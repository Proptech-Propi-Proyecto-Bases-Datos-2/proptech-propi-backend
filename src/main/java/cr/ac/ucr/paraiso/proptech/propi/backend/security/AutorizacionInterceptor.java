package cr.ac.ucr.paraiso.proptech.propi.backend.security;

import cr.ac.ucr.paraiso.proptech.propi.backend.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.List;

// Revisa en cada petición a /api/** que haya una sesión válida y que su rol
// tenga permiso sobre el recurso, siguiendo los permisos definidos en SQL Server (REQ 004 / REQ 008).
@Component
public class AutorizacionInterceptor implements HandlerInterceptor {

    // Recursos que el Digitador puede ver, crear y editar (sin borrar)
    private static final List<String> DIGITADOR_ESCRITURA = List.of(
            "/api/clientes", "/api/facturas", "/api/facturas-detalle", "/api/notas-credito-debito");
    // Catálogos que el Digitador solo puede leer para poder facturar
    private static final List<String> DIGITADOR_LECTURA = List.of(
            "/api/productos-servicios", "/api/tipos-impuestos");
    // Recursos del rol Consulta: solo reportería
    private static final List<String> CONSULTA_LECTURA = List.of("/api/reportes");

    private final SesionService sesionService;

    public AutorizacionInterceptor(SesionService sesionService) {
        this.sesionService = sesionService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws IOException {
        // Las peticiones preflight de CORS no llevan el token
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        Sesion sesion = sesionService.obtener(SesionService.extraerToken(request.getHeader("Authorization")));
        if (sesion == null) {
            return rechazar(response, HttpServletResponse.SC_UNAUTHORIZED, "Debe iniciar sesión.");
        }

        String ruta = request.getRequestURI().substring(request.getContextPath().length());
        if (!tienePermiso(sesion.rol(), request.getMethod(), ruta)) {
            return rechazar(response, HttpServletResponse.SC_FORBIDDEN,
                    "El usuario " + sesion.username() + " no tiene permiso para " + accion(request.getMethod())
                            + " " + recurso(ruta) + " por su rol (" + sesion.rol() + ").");
        }
        return true;
    }

    private static String accion(String metodo) {
        return switch (metodo.toUpperCase()) {
            case "GET" -> "consultar";
            case "POST" -> "registrar";
            case "PUT", "PATCH" -> "modificar";
            case "DELETE" -> "eliminar o inactivar";
            default -> "acceder a";
        };
    }

    // "/api/notas-credito-debito/5" -> "notas credito debito"
    private static String recurso(String ruta) {
        String[] partes = ruta.split("/");
        String nombre = partes.length > 2 ? partes[2] : ruta;
        if (nombre.equals("reportes")) return "los reportes";
        return nombre.replace('-', ' ');
    }

    static boolean tienePermiso(String rol, String metodo, String ruta) {
        boolean lectura = "GET".equalsIgnoreCase(metodo);
        return switch (rol) {
            case AuthService.ROL_ADMINISTRADOR -> true;
            case AuthService.ROL_DIGITADOR ->
                    (perteneceA(ruta, DIGITADOR_ESCRITURA) && !"DELETE".equalsIgnoreCase(metodo))
                            || (perteneceA(ruta, DIGITADOR_LECTURA) && lectura);
            case AuthService.ROL_CONSULTA -> perteneceA(ruta, CONSULTA_LECTURA) && lectura;
            default -> false;
        };
    }

    private static boolean perteneceA(String ruta, List<String> recursos) {
        return recursos.stream().anyMatch(r -> ruta.equals(r) || ruta.startsWith(r + "/"));
    }

    private boolean rechazar(HttpServletResponse response, int status, String mensaje) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        String json = mensaje.replace("\\", "\\\\").replace("\"", "\\\"");
        response.getWriter().write("{\"error\":\"" + json + "\"}");
        return false;
    }
}
