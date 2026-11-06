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
    void insertProductoServicio(@Param("cod_cabys") String codCabys,
                                @Param("id_impuesto") int idImpuesto,
                                @Param("descripcion") String descripcion,
                                @Param("unidad_medida") String unidadMedida,
                                @Param("precio") java.math.BigDecimal precio);

    @Transactional
    @Procedure(procedureName = "proptech.ActualizarProductoServicio")
    void actualizarProductoServicio(@Param("id_producto") int id, @Param("cod_cabys") String codCabys,
                                    @Param("id_impuesto") int idImpuesto, @Param("descripcion") String descripcion,
                                    @Param("unidad_medida") String unidadMedida,
                                    @Param("precio") java.math.BigDecimal precio);

    @Transactional
    @Procedure(procedureName = "proptech.DesactivarProductoServicio")
    void desactivarProductoServicio(@Param("id_producto") int id);
}
