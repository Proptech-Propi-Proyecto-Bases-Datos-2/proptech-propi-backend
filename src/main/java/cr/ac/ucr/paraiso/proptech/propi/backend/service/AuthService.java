package cr.ac.ucr.paraiso.proptech.propi.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;

@Service
public class AuthService {

    @Value("${spring.datasource.url}")
    private String dbUrl; 

    // Devuelve el id_bitacora si el login es correcto, o null si falla
    public Integer autenticarEnBD(String username, String password, String ipCliente) {
        try (Connection conn = DriverManager.getConnection(dbUrl, username, password)) {
            if (conn.isValid(2)) {
                return registrarIngresoBitacora(conn, username, ipCliente);
            }
            return null;
        } catch (SQLException e) {
            return null;
        }
    }

    private Integer registrarIngresoBitacora(Connection conn, String username, String ipCliente) {
        String sql = "{call proptech.RegistrarBitacoraAcceso(?, ?, ?, ?)}";
        
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            String terminal = InetAddress.getLocalHost().getHostName();
            
            stmt.setString(1, username);
            stmt.setString(2, terminal != null ? terminal : "UNKNOWN_TERMINAL");
            stmt.setString(3, ipCliente != null ? ipCliente : "127.0.0.1");
            
            // Registrar parámetro de salida OUTPUT
            stmt.registerOutParameter(4, Types.INTEGER);
            
            stmt.execute();
            
            return stmt.getInt(4); // Obtiene el id_bitacora generado por SQL Server
        } catch (Exception e) {
            System.err.println("Error al registrar el ingreso en la bitácora: " + e.getMessage());
            return null;
        }
    }

    public boolean registrarSalidaBitacora(Integer idBitacora, String username, String password) {
        if (idBitacora == null) return false;

        String sql = "{call proptech.RegistrarSalidaBitacoraAcceso(?)}";
        
        try (Connection conn = DriverManager.getConnection(dbUrl, username, password);
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setInt(1, idBitacora);
            stmt.execute();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al registrar la salida en la bitácora: " + e.getMessage());
            return false;
        }
    }
}