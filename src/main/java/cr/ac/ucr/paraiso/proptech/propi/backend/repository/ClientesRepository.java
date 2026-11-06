package cr.ac.ucr.paraiso.proptech.propi.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
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
        @Param("DireccionExacta") String direccionExacta,
        @Param("Profesion") String profesion,
        @Param("ActividadEconomica") String actividadEconomica,
        @Param("CorreoElectronico") String correoElectronico,
        @Param("Telefono") String telefono
    );

    @Query(value = "{call proptech.ConsultarClientes(:estado)}", nativeQuery = true)
    List<Object[]> consultarClientesNativo(@Param("estado") String estado);

    @Transactional
    @Procedure(procedureName = "proptech.ActualizarCliente")
    void actualizarCliente(
        @Param("p_id_cliente") Integer id,
        @Param("p_nombre") String nombre,
        @Param("p_num_identificacion") String numIdentificacion,
        @Param("p_tipo_identificacion") String tipoIdentificacion,
        @Param("p_provincia") String provincia,
        @Param("p_canton") String canton,
        @Param("p_distrito") String distrito,
        @Param("p_barrio") String barrio,
        @Param("p_direccion_exacta") String direccionExacta,
        @Param("p_profesion") String profesion,
        @Param("p_actividad_economica") String actividadEconomica,
        @Param("p_correo_electronico") String correoElectronico,
        @Param("p_telefono") String telefono,
        @Param("p_estado") String estado
    );

    @Transactional
    @Procedure(procedureName = "proptech.DesactivarCliente")
    void desactivarCliente(@Param("id_cliente") Integer id);
}
