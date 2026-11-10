package cr.ac.ucr.paraiso.proptech.propi.backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "NotasCreditoDebito", schema = "proptech")
public class NotasCreditoDebito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_nota")
    private int idNota;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_factura", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)   // la recibe, pero no la devuelve
    private Facturas factura;

    @Column(name = "fecha")
    private LocalDateTime fecha;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "motivo")
    private String motivo;

    @Column(name = "monto_total", nullable = false, precision = 18, scale = 2)
    private BigDecimal montoTotal;

    public NotasCreditoDebito() {}

    // Getters y Setters
    public int getIdNota() { return idNota; }
    public void setIdNota(int idNota) { this.idNota = idNota; }

    public Facturas getFactura() { return factura; }
    public void setFactura(Facturas factura) { this.factura = factura; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public BigDecimal getMontoTotal() { return montoTotal; }
    public void setMontoTotal(BigDecimal montoTotal) { this.montoTotal = montoTotal; }
}
