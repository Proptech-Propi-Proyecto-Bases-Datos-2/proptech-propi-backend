package cr.ac.ucr.paraiso.proptech.propi.backend.dto;

import java.time.LocalDateTime;

// Fila del reporte de pistas de auditoría (SP proptech.ReportePistasAuditoria)
public class ReporteAuditoriaDTO {

    private LocalDateTime fecRegistro;
    private String accion;
    private String nombreUsuario;
    private String tablaAfectada;
    private String llaveRegistro;
    private String datosAfectados;

    public ReporteAuditoriaDTO() {
    }

    public LocalDateTime getFecRegistro() {
        return fecRegistro;
    }

    public void setFecRegistro(LocalDateTime fecRegistro) {
        this.fecRegistro = fecRegistro;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getTablaAfectada() {
        return tablaAfectada;
    }

    public void setTablaAfectada(String tablaAfectada) {
        this.tablaAfectada = tablaAfectada;
    }

    public String getLlaveRegistro() {
        return llaveRegistro;
    }

    public void setLlaveRegistro(String llaveRegistro) {
        this.llaveRegistro = llaveRegistro;
    }

    public String getDatosAfectados() {
        return datosAfectados;
    }

    public void setDatosAfectados(String datosAfectados) {
        this.datosAfectados = datosAfectados;
    }
}
