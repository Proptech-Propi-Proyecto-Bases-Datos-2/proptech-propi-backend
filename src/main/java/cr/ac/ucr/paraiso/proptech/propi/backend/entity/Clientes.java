package cr.ac.ucr.paraiso.proptech.propi.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Clientes", schema = "proptech")
public class Clientes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private int idCliente;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "num_identificacion", columnDefinition = "VARBINARY(256)")
    private byte[] numIdentificacion;

    @Column(name = "tipo_identificacion")
    private String tipoIdentificacion;

    @Column(name = "provincia")
    private String provincia;

    @Column(name = "canton")
    private String canton;

    @Column(name = "distrito")
    private String distrito;

    @Column(name = "barrio")
    private String barrio;

    @Column(name = "profesion")
    private String profesion;

    @Column(name = "actividad_economica")
    private String actividadEconomica;

    @Column(name = "correo_electronico", nullable = false, columnDefinition = "VARBINARY(256)")
    private byte[] correoElectronico;

    @Column(name = "telefono", nullable = false, columnDefinition = "VARBINARY(256)")
    private byte[] telefono;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @Column(name = "estado")
    private String estado;

    @OneToMany(mappedBy = "cliente")
    private List<Facturas> facturas = new ArrayList<>();

    public Clientes() {}

    // Getters y Setters
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public byte[] getNumIdentificacion() { return numIdentificacion; }
    public void setNumIdentificacion(byte[] numIdentificacion) { this.numIdentificacion = numIdentificacion; }

    public String getTipoIdentificacion() { return tipoIdentificacion; }
    public void setTipoIdentificacion(String tipoIdentificacion) { this.tipoIdentificacion = tipoIdentificacion; }

    public String getProvincia() { return provincia; }
    public void setProvincia(String provincia) { this.provincia = provincia; }

    public String getCanton() { return canton; }
    public void setCanton(String canton) { this.canton = canton; }

    public String getDistrito() { return distrito; }
    public void setDistrito(String distrito) { this.distrito = distrito; }

    public String getBarrio() { return barrio; }
    public void setBarrio(String barrio) { this.barrio = barrio; }

    public String getProfesion() { return profesion; }
    public void setProfesion(String profesion) { this.profesion = profesion; }

    public String getActividadEconomica() { return actividadEconomica; }
    public void setActividadEconomica(String actividadEconomica) { this.actividadEconomica = actividadEconomica; }

    public byte[] getCorreoElectronico() { return correoElectronico; }
    public void setCorreoElectronico(byte[] correoElectronico) { this.correoElectronico = correoElectronico; }

    public byte[] getTelefono() { return telefono; }
    public void setTelefono(byte[] telefono) { this.telefono = telefono; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public List<Facturas> getFacturas() { return facturas; }
    public void setFacturas(List<Facturas> facturas) { this.facturas = facturas; }

    public void agregarFactura(Facturas factura) {
        facturas.add(factura);
        factura.setCliente(this);
    }

}
