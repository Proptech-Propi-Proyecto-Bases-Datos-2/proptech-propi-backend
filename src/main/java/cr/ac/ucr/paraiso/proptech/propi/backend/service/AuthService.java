package cr.ac.ucr.paraiso.proptech.propi.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Service
public class AuthService {

    @Value("${spring.datasource.url}")
    private String dbUrl; 

    public boolean autenticarEnBD(String username, String password) {
        // Al NO tener integratedSecurity=true, JDBC enviará exactamente el username y password a SQL Server
        try (Connection conn = DriverManager.getConnection(dbUrl, username, password)) {
            return conn.isValid(2); 
        } catch (SQLException e) {
            // Si la contraseña o usuario son incorrectos en SQL Server, cae aquí
            return false;
        }
    }
}