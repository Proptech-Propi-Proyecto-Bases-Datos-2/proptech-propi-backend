package cr.ac.ucr.paraiso.proptech.propi.backend.repository;

import cr.ac.ucr.paraiso.proptech.propi.backend.entity.Facturas;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

public interface FacturasRepository extends JpaRepository<Facturas, Integer> {

    @Transactional
    @Procedure(
        procedureName = "proptech.InsertarFactura",
        outputParameterName = "id_factura_out"
    )
    int insertFactura(
        @Param("id_cliente") int idCliente,
        @Param("condicion_venta") String condicionVenta,
        @Param("medio_pago") String medioPago,
        @Param("moneda") String moneda,
        @Param("tipo_cambio") java.math.BigDecimal tipoCambio,
        @Param("observaciones") String observaciones,
        @Param("subtotal") java.math.BigDecimal subtotal,
        @Param("total_impuestos") java.math.BigDecimal totalImpuestos,
        @Param("monto_total") java.math.BigDecimal montoTotal
    );
}
