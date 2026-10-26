package cr.ac.ucr.paraiso.proptech.propi.backend.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cr.ac.ucr.paraiso.proptech.propi.backend.service.AuthService;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200") // Permitir peticiones desde Angular
public class AuthRestController {

    private final AuthService authService;

    public AuthRestController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        boolean esValido = authService.autenticarEnBD(username, password);

        if (esValido) {
            return ResponseEntity.ok(Map.of(
                "mensaje", "Autenticación exitosa",
                "username", username
            ));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Usuario o contraseña incorrectos en SQL Server");
        }
    }
}
