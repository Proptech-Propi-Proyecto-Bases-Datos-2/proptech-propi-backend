package cr.ac.ucr.paraiso.proptech.propi.backend.service;

import cr.ac.ucr.paraiso.proptech.propi.backend.security.Sesion;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

@Service
public class AuthService {

    public static final String ROL_ADMINISTRADOR = "Administrador";
    public static final String ROL_DIGITADOR = "Digitador";
    public static final String ROL_CONSULTA = "Consulta";

    @Value("${spring.datasource.url}")
    private String dbUrl;

    private final JdbcTemplate jdbcTemplate;

    public AuthService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Valida el usuario contra SQL Server, obtiene su rol y registra el ingreso en la bitácora.
    // Devuelve null si las credenciales son incorrectas o el usuario no pertenece a ningún rol de la app.
    public Sesion autenticarEnBD(String username, String password, String ipCliente) {
        try (Connection conn = DriverManager.getConnection(dbUrl, username, password)) {
            String rol = obtenerRol(conn);
            if (rol == null) {
                System.err.println("El usuario " + username + " no pertenece a ningún rol de la aplicación");
                return null;
            }
            Integer idBitacora = registrarIngresoBitacora(conn, username, ipCliente);
            if (idBitacora == null) {
                return null;
            }
            return new Sesion(username, rol, idBitacora);
        } catch (SQLException e) {
            System.err.println("Error de autenticacion SQL Server: " + e.getMessage());
            return null;
        }
    }

    // Pregunta a SQL Server, con la conexión del propio usuario, a cuál rol pertenece
    private String obtenerRol(Connection conn) throws SQLException {
        String sql = "SELECT IS_ROLEMEMBER('Administrador') AS adm, "
                + "IS_ROLEMEMBER('Digitador') AS dig, "
                + "IS_ROLEMEMBER('Consulta') AS con";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (!rs.next()) return null;
            if (rs.getInt("adm") == 1) return ROL_ADMINISTRADOR;
            if (rs.getInt("dig") == 1) return ROL_DIGITADOR;
            if (rs.getInt("con") == 1) return ROL_CONSULTA;
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

    // Registra la salida con la conexión del backend, así el frontend no necesita guardar la contraseña
    public boolean registrarSalidaBitacora(Integer idBitacora) {
        if (idBitacora == null) return false;

        try {
            jdbcTemplate.update("{call proptech.RegistrarSalidaBitacoraAcceso(?)}", idBitacora);
            return true;
        } catch (Exception e) {
            System.err.println("Error al registrar la salida en la bitácora: " + e.getMessage());
            return false;
        }
    }
}
