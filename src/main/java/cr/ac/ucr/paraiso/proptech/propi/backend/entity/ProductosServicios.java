package cr.ac.ucr.paraiso.proptech.propi.backend.entity;


import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "ProductosServicios", schema = "proptech")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ProductosServicios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private int idProducto;

    @Column(name = "cod_cabys", nullable = false, length = 13)
    private String codCabys;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_impuesto", nullable = false)
    private TiposImpuesto tipoImpuesto;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "unidad_medida")
    private String unidadMedida;

    @Column(name = "precio", nullable = false, precision = 18, scale = 2)
    private BigDecimal precio;

    @Column(name = "estado")
    private String estado;

    @JsonIgnore
    @OneToMany(mappedBy = "productoServicio")
    private List<FacturasDetalle> detalles = new ArrayList<>();

    public ProductosServicios() {}

    // Getters y Setters
    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public String getCodCabys() { return codCabys; }
    public void setCodCabys(String codCabys) { this.codCabys = codCabys; }

    public TiposImpuesto getTipoImpuesto() { return tipoImpuesto; }
    public void setTipoImpuesto(TiposImpuesto tipoImpuesto) { this.tipoImpuesto = tipoImpuesto; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getUnidadMedida() { return unidadMedida; }
    public void setUnidadMedida(String unidadMedida) { this.unidadMedida = unidadMedida; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public List<FacturasDetalle> getDetalles() { return detalles; }
    public void setDetalles(List<FacturasDetalle> detalles) { this.detalles = detalles; }

    public void agregarDetalle(FacturasDetalle detalle) {
        detalles.add(detalle);
        detalle.setProductoServicio(this);
    }
}
