package cr.ac.ucr.paraiso.proptech.propi.backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Facturas", schema = "proptech")
public class Facturas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_factura")
    private int idFactura;

    @ManyToOne(fetch = FetchType.LAZY)//revisar si falta
    @JoinColumn(name = "id_cliente", nullable = false)
    private Clientes cliente;

    @Column(name = "fecha_factura")
    private LocalDateTime fechaFactura;

    @Column(name = "condicion_venta")
    private String condicionVenta;

    @Column(name = "medio_pago")
    private String medioPago;

    @Column(name = "moneda")
    private String moneda;

    @Column(name = "tipo_cambio")
    private BigDecimal tipoCambio = new BigDecimal("1.0000");

    @Column(name = "observaciones")
    private String observaciones;

    @Column(name = "subtotal")
    private BigDecimal subtotal;

    @Column(name = "total_impuestos")
    private BigDecimal totalImpuestos;

    @Column(name = "monto_total", nullable = false, precision = 18, scale = 2)
    private BigDecimal montoTotal;

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FacturasDetalle> detalles = new ArrayList<>();

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NotasCreditoDebito> notas = new ArrayList<>();

    public Facturas() {}

    // Getters y Setters
    public int getIdFactura() { return idFactura; }
    public void setIdFactura(int idFactura) { this.idFactura = idFactura; }

    public Clientes getCliente() { return cliente; }
    public void setCliente(Clientes cliente) { this.cliente = cliente; }

    public LocalDateTime getFechaFactura() { return fechaFactura; }

    public String getCondicionVenta() { return condicionVenta; }
    public void setCondicionVenta(String condicionVenta) { this.condicionVenta = condicionVenta; }

    public String getMedioPago() { return medioPago; }
    public void setMedioPago(String medioPago) { this.medioPago = medioPago; }

    public String getMoneda() { return moneda; }
    public void setMoneda(String moneda) { this.moneda = moneda; }

    public BigDecimal getTipoCambio() { return tipoCambio; }
    public void setTipoCambio(BigDecimal tipoCambio) { this.tipoCambio = tipoCambio; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public BigDecimal getTotalImpuestos() { return totalImpuestos; }
    public void setTotalImpuestos(BigDecimal totalImpuestos) { this.totalImpuestos = totalImpuestos; }

    public BigDecimal getMontoTotal() { return montoTotal; }
    public void setMontoTotal(BigDecimal montoTotal) { this.montoTotal = montoTotal; }

    public List<FacturasDetalle> getDetalles() { return detalles; }
    public void setDetalles(List<FacturasDetalle> detalles) { this.detalles = detalles; }

    public void agregarDetalle(FacturasDetalle detalle) {
        detalles.add(detalle);
        detalle.setFactura(this);
    }

    public List<NotasCreditoDebito> getNotas() { return notas; }
    public void setNotas(List<NotasCreditoDebito> notas) { this.notas = notas; }

    public void agregarNota(NotasCreditoDebito nota) {
        notas.add(nota);
        nota.setFactura(this);
    }
}
