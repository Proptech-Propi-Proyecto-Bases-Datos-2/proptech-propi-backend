package cr.ac.ucr.paraiso.proptech.propi.backend.dto;

// Fila del reporte de configuración de seguridad (SP proptech.ReporteConfiguracionSeguridad)
public class ReporteSeguridadDTO {

    private String nombreUsuario;
    private String rol;
    private String nombreObjeto;
    private String tipoObjeto;
    private String tipoPermiso;
    private String estadoPermiso;

    public ReporteSeguridadDTO() {
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getNombreObjeto() {
        return nombreObjeto;
    }

    public void setNombreObjeto(String nombreObjeto) {
        this.nombreObjeto = nombreObjeto;
    }

    public String getTipoObjeto() {
        return tipoObjeto;
    }

    public void setTipoObjeto(String tipoObjeto) {
        this.tipoObjeto = tipoObjeto;
    }

    public String getTipoPermiso() {
        return tipoPermiso;
    }

    public void setTipoPermiso(String tipoPermiso) {
        this.tipoPermiso = tipoPermiso;
    }

    public String getEstadoPermiso() {
        return estadoPermiso;
    }

    public void setEstadoPermiso(String estadoPermiso) {
        this.estadoPermiso = estadoPermiso;
    }
}
