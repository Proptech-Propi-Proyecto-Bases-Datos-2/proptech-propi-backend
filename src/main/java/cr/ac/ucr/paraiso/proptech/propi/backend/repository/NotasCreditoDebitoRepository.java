package cr.ac.ucr.paraiso.proptech.propi.backend.repository;

import cr.ac.ucr.paraiso.proptech.propi.backend.entity.NotasCreditoDebito;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

public interface NotasCreditoDebitoRepository extends JpaRepository<NotasCreditoDebito, Integer> {

    @Transactional
    @Procedure(
        procedureName = "proptech.InsertarNotaCreditoDebito",
        outputParameterName = "IdNotaCreditoDebito"
    )
    void insertNota(@Param("idFactura") int idFactura,
                    @Param("fecha") java.time.LocalDateTime fecha,
                    @Param("tipo") String tipo,
                    @Param("motivo") String motivo,
                    @Param("montoTotal") java.math.BigDecimal montoTotal);
}
