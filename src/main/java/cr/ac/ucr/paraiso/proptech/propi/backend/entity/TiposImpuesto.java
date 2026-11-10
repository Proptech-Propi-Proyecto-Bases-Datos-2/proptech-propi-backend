package cr.ac.ucr.paraiso.proptech.propi.backend.entity;


import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "TiposImpuesto", schema = "proptech")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TiposImpuesto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_impuesto")
    private int idImpuesto;

    @Column(name = "tarifa")
    private String tarifa;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "porcentaje", nullable = false, precision = 5, scale = 2)
    private BigDecimal porcentaje;

    @Column(name = "estado")
    private String estado;

    @OneToMany(mappedBy = "tipoImpuesto")
    @JsonIgnore 
    private List<ProductosServicios> productosServicios = new ArrayList<>();

    public TiposImpuesto() {}

    // Getters y Setters
    public int getIdImpuesto() { return idImpuesto; }
    public void setIdImpuesto(int idImpuesto) { this.idImpuesto = idImpuesto; }

    public String getTarifa() { return tarifa; }
    public void setTarifa(String tarifa) { this.tarifa = tarifa; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public BigDecimal getPorcentaje() { return porcentaje; }
    public void setPorcentaje(BigDecimal porcentaje) { this.porcentaje = porcentaje; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public List<ProductosServicios> getProductosServicios() { return productosServicios; }
    public void setProductosServicios(List<ProductosServicios> productosServicios) {
        this.productosServicios = productosServicios;
    }

    public void agregarProductoServicio(ProductosServicios productoServicio) {
        productosServicios.add(productoServicio);
        productoServicio.setTipoImpuesto(this);
    }
}