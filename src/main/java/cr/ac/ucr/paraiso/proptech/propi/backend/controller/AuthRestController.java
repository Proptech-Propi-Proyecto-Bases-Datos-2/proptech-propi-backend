package cr.ac.ucr.paraiso.proptech.propi.backend.controller;

import cr.ac.ucr.paraiso.proptech.propi.backend.security.Sesion;
import cr.ac.ucr.paraiso.proptech.propi.backend.security.SesionService;
import cr.ac.ucr.paraiso.proptech.propi.backend.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200") // Permitir llamadas desde Angular
public class AuthRestController {

    private final AuthService authService;
    private final SesionService sesionService;

    public AuthRestController(AuthService authService, SesionService sesionService) {
        this.authService = authService;
        this.sesionService = sesionService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials, HttpServletRequest request) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        String clientIp = request.getHeader("X-Forwarded-For");
        if (clientIp == null || clientIp.isEmpty()) {
            clientIp = request.getRemoteAddr();
        }

        Sesion sesion = authService.autenticarEnBD(username, password, clientIp);

        if (sesion == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Usuario o contraseña incorrectos en SQL Server");
        }

        String token = sesionService.crear(sesion);
        return ResponseEntity.ok(Map.of(
                "mensaje", "Autenticación exitosa",
                "username", sesion.username(),
                "rol", sesion.rol(),
                "idBitacora", sesion.idBitacora(),
                "token", token
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(value = "Authorization", required = false) String authorization) {
        Sesion sesion = sesionService.eliminar(SesionService.extraerToken(authorization));
        if (sesion == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "La sesión no existe o ya expiró."));
        }

        if (authService.registrarSalidaBitacora(sesion.idBitacora())) {
            return ResponseEntity.ok(Map.of("mensaje", "Cierre de sesión registrado correctamente"));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "No se pudo actualizar la fecha de salida en la bitácora"));
    }
}
