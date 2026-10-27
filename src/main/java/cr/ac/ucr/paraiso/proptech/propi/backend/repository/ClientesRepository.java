package cr.ac.ucr.paraiso.proptech.propi.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import cr.ac.ucr.paraiso.proptech.propi.backend.entity.Clientes;
import jakarta.transaction.Transactional;

public interface ClientesRepository extends JpaRepository<Clientes, Integer> {

    
    @Transactional
    @Procedure(
        procedureName = "proptech.InsertarCliente",
        outputParameterName = "IdCliente"
    )
    int insertClient(
        @Param("Nombre") String nombre,
        @Param("NumIdentificacion") String numIdentificacion,
        @Param("TipoIdentificacion") String tipoIdentificacion,
        @Param("Provincia") String provincia,
        @Param("Canton") String canton,
        @Param("Distrito") String distrito,
        @Param("Barrio") String barrio,
        @Param("Profesion") String profesion,
        @Param("ActividadEconomica") String actividadEconomica,
        @Param("CorreoElectronico") String correoElectronico,
        @Param("Telefono") String telefono
    );

    @Query(value = "{call proptech.ConsultarClientes(:estado)}", nativeQuery = true)
    List<Object[]> consultarClientesNativo(@Param("estado") String estado);

    @Modifying
    @Query(value = "{call proptech.sp_ActualizarCliente(:id, :nombre, :numIdent, :tipoIdent, :prov, :cant, :dist, :barr, :prof, :actEcon, :correo, :tel, :estado)}", nativeQuery = true)
    void actualizarClienteSP(
        @Param("id") Integer id,
        @Param("nombre") String nombre,
        @Param("numIdent") String numIdentificacion,
        @Param("tipoIdent") String tipoIdentificacion,
        @Param("prov") String provincia,
        @Param("cant") String canton,
        @Param("dist") String distrito,
        @Param("barr") String barrio,
        @Param("prof") String profesion,
        @Param("actEcon") String actividadEconomica,
        @Param("correo") String correoElectronico,
        @Param("tel") String telefono,
        @Param("estado") String estado
    );
}
