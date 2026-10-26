package cr.ac.ucr.paraiso.proptech.propi.backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "FacturasDetalle", schema = "proptech")
public class FacturasDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private int idDetalle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_factura", nullable = false)
    private Facturas factura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    private ProductosServicios productoServicio;

    @Column(name = "cantidad")
    private int cantidad;

    @Column(name = "monto_linea", nullable = false, precision = 18, scale = 2)
    private BigDecimal montoLinea;

    public FacturasDetalle() {}

    // Getters y Setters
    public int getIdDetalle() { return idDetalle; }
    public void setIdDetalle(int idDetalle) { this.idDetalle = idDetalle; }

    public Facturas getFactura() { return factura; }
    public void setFactura(Facturas factura) { this.factura = factura; }

    public ProductosServicios getProductoServicio() { return productoServicio; }
    public void setProductoServicio(ProductosServicios productoServicio) { this.productoServicio = productoServicio; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public BigDecimal getMontoLinea() { return montoLinea; }
    public void setMontoLinea(BigDecimal montoLinea) { this.montoLinea = montoLinea; }
}
