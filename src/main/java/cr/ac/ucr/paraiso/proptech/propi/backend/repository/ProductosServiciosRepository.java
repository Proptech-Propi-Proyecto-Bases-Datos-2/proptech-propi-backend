package cr.ac.ucr.paraiso.proptech.propi.backend.repository;

import cr.ac.ucr.paraiso.proptech.propi.backend.entity.ProductosServicios;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

public interface ProductosServiciosRepository extends JpaRepository<ProductosServicios, Integer> {

    @Transactional
    @Procedure(
        procedureName = "proptech.InsertarProductoServicio",
        outputParameterName = "IdProductoServicio"
    )
    void insertProductoServicio(@Param("codCabys") String codCabys,
                                @Param("idImpuesto") int idImpuesto,
                                @Param("descripcion") String descripcion,
                                @Param("unidadMedida") String unidadMedida,
                                @Param("precio") java.math.BigDecimal precio,
                                @Param("estado") String estado);
}
