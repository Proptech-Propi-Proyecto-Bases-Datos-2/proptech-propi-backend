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
        outputParameterName = "IdFactura"
    )
    void insertFactura(@Param("idCliente") int idCliente,
                       @Param("fechaFactura") java.time.LocalDateTime fechaFactura,
                       @Param("condicionVenta") String condicionVenta,
                       @Param("medioPago") String medioPago,
                       @Param("moneda") String moneda,
                       @Param("tipoCambio") java.math.BigDecimal tipoCambio,
                       @Param("observaciones") String observaciones,
                       @Param("subtotal") java.math.BigDecimal subtotal,
                       @Param("totalImpuestos") java.math.BigDecimal totalImpuestos,
                       @Param("montoTotal") java.math.BigDecimal montoTotal);
}
