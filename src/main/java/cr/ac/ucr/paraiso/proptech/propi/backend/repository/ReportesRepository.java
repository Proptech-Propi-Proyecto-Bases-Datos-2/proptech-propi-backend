package cr.ac.ucr.paraiso.proptech.propi.backend.repository;

import cr.ac.ucr.paraiso.proptech.propi.backend.dto.ReporteAuditoriaDTO;
import cr.ac.ucr.paraiso.proptech.propi.backend.dto.ReporteClienteDTO;
import cr.ac.ucr.paraiso.proptech.propi.backend.dto.ReporteFacturaDTO;
import cr.ac.ucr.paraiso.proptech.propi.backend.dto.ReporteSeguridadDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

// Ejecuta los 4 procedimientos almacenados de reportes (REQ 007).
// Los valores se envían como parámetros (?) y nunca se pegan al texto del SQL, así se evita la inyección SQL.
@Repository
public class ReportesRepository {

    private final JdbcTemplate jdbcTemplate;

    public ReportesRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Reporte 1: clientes registrados entre dos fechas, filtrados por estado (A, I o T)
    public List<ReporteClienteDTO> reporteClientes(LocalDate desde, LocalDate hasta, String estado) {
        String sql = "EXEC proptech.ReporteClientesResumido @p_fecha_desde = ?, @p_fecha_hasta = ?, @p_estado = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ReporteClienteDTO dto = new ReporteClienteDTO();
            dto.setNombre(rs.getString("nombre"));
            dto.setNumIdentificacion(rs.getString("num_identificacion"));
            dto.setActividadEconomica(rs.getString("actividad_economica"));
            dto.setCorreoElectronico(rs.getString("correo_electronico"));
            dto.setTelefono(rs.getString("telefono"));
            return dto;
        }, Date.valueOf(desde), Date.valueOf(hasta), estado);
    }

    // Reporte 2: facturas emitidas entre dos fechas
    public List<ReporteFacturaDTO> reporteFacturas(LocalDate desde, LocalDate hasta) {
        String sql = "EXEC proptech.ReporteFacturas @p_fecha_desde = ?, @p_fecha_hasta = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ReporteFacturaDTO dto = new ReporteFacturaDTO();
            dto.setNombreCliente(rs.getString("nombre_cliente"));
            dto.setNumIdentificacion(rs.getString("num_identificacion"));
            dto.setActividadEconomica(rs.getString("actividad_economica"));
            dto.setNumeroFactura(rs.getInt("numero_factura"));
            dto.setFechaFactura(toLocalDateTime(rs.getTimestamp("fecha_factura")));
            dto.setSubtotal(rs.getBigDecimal("subtotal"));
            dto.setTotalImpuestos(rs.getBigDecimal("total_impuestos"));
            dto.setMontoTotal(rs.getBigDecimal("monto_total"));
            return dto;
        }, Date.valueOf(desde), Date.valueOf(hasta));
    }

    // Reporte 3: pistas de auditoría entre dos fechas, de un usuario o de todos (usuario = null)
    public List<ReporteAuditoriaDTO> reporteAuditoria(LocalDate desde, LocalDate hasta, String usuario) {
        String sql = "EXEC proptech.ReportePistasAuditoria @p_fecha_desde = ?, @p_fecha_hasta = ?, @p_usuario = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ReporteAuditoriaDTO dto = new ReporteAuditoriaDTO();
            dto.setFecRegistro(toLocalDateTime(rs.getTimestamp("fec_registro")));
            dto.setAccion(rs.getString("accion"));
            dto.setNombreUsuario(rs.getString("nombre_usuario"));
            dto.setTablaAfectada(rs.getString("tabla_afectada"));
            dto.setLlaveRegistro(rs.getString("llave_registro"));
            dto.setDatosAfectados(rs.getString("datos_afectados"));
            return dto;
        }, Date.valueOf(desde), Date.valueOf(hasta), usuario);
    }

    // Reporte 4: permisos de cada usuario según su rol, de un usuario o de todos (usuario = null)
    public List<ReporteSeguridadDTO> reporteSeguridad(String usuario) {
        String sql = "EXEC proptech.ReporteConfiguracionSeguridad @p_usuario = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ReporteSeguridadDTO dto = new ReporteSeguridadDTO();
            dto.setNombreUsuario(rs.getString("nombre_usuario"));
            dto.setRol(rs.getString("rol"));
            dto.setNombreObjeto(rs.getString("nombre_objeto"));
            dto.setTipoObjeto(rs.getString("tipo_objeto"));
            dto.setTipoPermiso(rs.getString("tipo_permiso"));
            dto.setEstadoPermiso(rs.getString("estado_permiso"));
            return dto;
        }, usuario);
    }

    // Convierte la fecha de SQL Server (Timestamp) a LocalDateTime de Java
    private LocalDateTime toLocalDateTime(Timestamp value) {
        return value == null ? null : value.toLocalDateTime();
    }
}
