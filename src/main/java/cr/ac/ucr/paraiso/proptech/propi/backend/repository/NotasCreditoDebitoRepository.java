package cr.ac.ucr.paraiso.proptech.propi.backend.repository;

import cr.ac.ucr.paraiso.proptech.propi.backend.entity.NotasCreditoDebito;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

public interface NotasCreditoDebitoRepository extends JpaRepository<NotasCreditoDebito, Integer> {

    @Transactional
    @Procedure(
        procedureName = "proptech.CrearNota"
    )
    void insertNota(@Param("id_factura") int idFactura,
                    @Param("tipo") String tipo,
                    @Param("motivo") String motivo,
                    @Param("monto_total") java.math.BigDecimal montoTotal);
}
