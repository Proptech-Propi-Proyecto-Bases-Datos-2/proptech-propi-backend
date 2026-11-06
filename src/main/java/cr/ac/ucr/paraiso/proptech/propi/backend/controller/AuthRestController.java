package cr.ac.ucr.paraiso.proptech.propi.backend.controller;

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

    public AuthRestController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials, HttpServletRequest request) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        String clientIp = request.getHeader("X-Forwarded-For");
        if (clientIp == null || clientIp.isEmpty()) {
            clientIp = request.getRemoteAddr();
        }

        Integer idBitacora = authService.autenticarEnBD(username, password, clientIp);

        if (idBitacora != null) {
            return ResponseEntity.ok(Map.of(
                "mensaje", "Autenticación exitosa",
                "username", username,
                "idBitacora", idBitacora
            ));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Usuario o contraseña incorrectos en SQL Server");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody Map<String, Object> body) {
        Integer idBitacora = (Integer) body.get("idBitacora");
        String username = (String) body.get("username");
        String password = (String) body.get("password");

        boolean exito = authService.registrarSalidaBitacora(idBitacora, username, password);

        if (exito) {
            return ResponseEntity.ok(Map.of("mensaje", "Cierre de sesión registrado correctamente"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("No se pudo actualizar la fecha de salida en la bitácora");
        }
    }
}