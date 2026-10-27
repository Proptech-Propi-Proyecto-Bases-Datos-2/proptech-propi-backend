package cr.ac.ucr.paraiso.proptech.propi.backend.repository;

import cr.ac.ucr.paraiso.proptech.propi.backend.entity.FacturasDetalle;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

public interface FacturasDetalleRepository extends JpaRepository<FacturasDetalle, Integer> {

    @Transactional
    @Procedure(
        procedureName = "proptech.InsertarFacturaDetalle",
        outputParameterName = "IdFacturaDetalle"
    )
    void insertFacturaDetalle(@Param("idFactura") int idFactura,
                              @Param("idProducto") int idProducto,
                              @Param("cantidad") int cantidad,
                              @Param("montoLinea") java.math.BigDecimal montoLinea);
}
