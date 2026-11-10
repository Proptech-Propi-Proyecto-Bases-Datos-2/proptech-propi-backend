package cr.ac.ucr.paraiso.proptech.propi.backend.repository;

import cr.ac.ucr.paraiso.proptech.propi.backend.entity.FacturasDetalle;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

public interface FacturasDetalleRepository extends JpaRepository<FacturasDetalle, Integer> {

    @Transactional
    @Procedure(procedureName = "proptech.InsertarFacturaDetalle")
    void insertFacturaDetalle(
        @Param("id_factura") int idFactura,
        @Param("id_producto") int idProducto,
        @Param("cantidad") int cantidad,
        @Param("monto_linea") java.math.BigDecimal montoLinea
    );
}
