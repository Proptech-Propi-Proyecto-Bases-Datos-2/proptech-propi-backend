package cr.ac.ucr.paraiso.proptech.propi.backend.security;

// Datos de una sesión activa: quién es, qué rol tiene en SQL Server y su registro en la bitácora
public record Sesion(String username, String rol, Integer idBitacora) {
}
