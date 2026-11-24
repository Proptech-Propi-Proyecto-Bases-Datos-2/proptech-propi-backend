package cr.ac.ucr.paraiso.proptech.propi.backend.security;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

// Guarda en memoria las sesiones activas. Cada login recibe un token aleatorio
// que el frontend envía en el encabezado Authorization: Bearer <token>.
@Service
public class SesionService {

    private final Map<String, Sesion> sesiones = new ConcurrentHashMap<>();

    public String crear(Sesion sesion) {
        String token = UUID.randomUUID().toString();
        sesiones.put(token, sesion);
        return token;
    }

    public Sesion obtener(String token) {
        return token == null ? null : sesiones.get(token);
    }

    public Sesion eliminar(String token) {
        return token == null ? null : sesiones.remove(token);
    }

    // Extrae el token de un encabezado "Bearer <token>"
    public static String extraerToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return null;
        }
        return authorizationHeader.substring(7).trim();
    }
}
