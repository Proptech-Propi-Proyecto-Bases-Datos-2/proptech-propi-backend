package cr.ac.ucr.paraiso.proptech.propi.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import cr.ac.ucr.paraiso.proptech.propi.backend.entity.Clientes;
import jakarta.transaction.Transactional;

public interface ClientesRepository extends JpaRepository<Clientes, Integer> {

    @Transactional 
    @Modifying 
    @Query (value = """
        EXEC proptech.InsertarCliente
            @Nombre = :nombre,
            @NumIdentificacion = :numIdentificacion,
            @TipoIdentificacion = :tipoIdentificacion,
            @Provincia = :provincia,
            @Canton = :canton,
            @Distrito = :distrito,
            @Barrio = :barrio,
            @Profesion = :profesion,
            @ActividadEconomica = :actividadEconomica,
            @CorreoElectronico = :correoElectronico,
            @Telefono = :telefono
        """, nativeQuery = true)
    void insertClient(
            @Param ("nombre") String nombre,
            @Param("numIdentificacion") String numIdentificacion,
            @Param("tipoIdentificacion") String tipoIdentificacion,
            @Param("provincia") String provincia,
            @Param("canton") String canton,
            @Param("distrito") String distrito,
            @Param("barrio") String barrio,
            @Param("profesion") String profesion,
            @Param("actividadEconomica") String actividadEconomica,
            @Param("correoElectronico") String correoElectronico,
            @Param("telefono") String telefono
    );
}
