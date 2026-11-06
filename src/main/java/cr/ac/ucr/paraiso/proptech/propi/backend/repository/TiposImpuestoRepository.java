package cr.ac.ucr.paraiso.proptech.propi.backend.repository;

import cr.ac.ucr.paraiso.proptech.propi.backend.entity.TiposImpuesto;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

public interface TiposImpuestoRepository extends JpaRepository<TiposImpuesto, Integer> {

    @Transactional
    @Procedure(
        procedureName = "proptech.InsertarTipoImpuesto",
        outputParameterName = "IdTipoImpuesto"
    )
    void insertTipoImpuesto(@Param("tarifa") String tarifa,
                            @Param("descripcion") String descripcion,
                            @Param("porcentaje") java.math.BigDecimal porcentaje);

    @Transactional
    @Procedure(procedureName = "proptech.ActualizarTipoImpuesto")
    void actualizarTipoImpuesto(@Param("id_impuesto") int id, @Param("tarifa") String tarifa,
                                @Param("descripcion") String descripcion,
                                @Param("porcentaje") java.math.BigDecimal porcentaje);

    @Transactional
    @Procedure(procedureName = "proptech.DesactivarTipoImpuesto")
    void desactivarTipoImpuesto(@Param("id_impuesto") int id);
}
