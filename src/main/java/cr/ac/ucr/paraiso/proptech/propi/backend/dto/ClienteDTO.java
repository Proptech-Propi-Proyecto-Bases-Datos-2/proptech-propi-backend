package cr.ac.ucr.paraiso.proptech.propi.backend.dto;

import java.time.LocalDateTime;

public class ClienteDTO {

    private int idCliente;
    private String nombre;
    private String numIdentificacion;
    private String tipoIdentificacion;
    private String provincia;
    private String canton;
    private String distrito;
    private String barrio;
    private String profesion;
    private String actividadEconomica;
    private String correoElectronico;
    private String telefono;
    private LocalDateTime fechaRegistro;
    private String estado;

    public ClienteDTO() {
    }

    public ClienteDTO(String nombre, String numIdentificacion, String tipoIdentificacion, String provincia,
            String canton, String distrito, String barrio, String profesion, String actividadEconomica,
            String correoElectronico, String telefono) {
        this.nombre = nombre;
        this.numIdentificacion = numIdentificacion;
        this.tipoIdentificacion = tipoIdentificacion;
        this.provincia = provincia;
        this.canton = canton;
        this.distrito = distrito;
        this.barrio = barrio;
        this.profesion = profesion;
        this.actividadEconomica = actividadEconomica;
        this.correoElectronico = correoElectronico;
        this.telefono = telefono;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNumIdentificacion(String numIdentificacion) {
        this.numIdentificacion = numIdentificacion;
    }

    public String getNumIdentificacion() {
        return numIdentificacion;
    }

    public void setTipoIdentificacion(String tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public String getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

    public String getCanton() {
        return canton;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }   

    public String getBarrio() {
        return barrio;
    }

    public void setProfesion(String profesion) {
        this.profesion = profesion;
    }       

    public String getProfesion() {
        return profesion;
    }

    public void setActividadEconomica(String actividadEconomica) {
        this.actividadEconomica = actividadEconomica;
    }

    public String getActividadEconomica() {
        return actividadEconomica;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEstado() {
        return estado;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }
}

