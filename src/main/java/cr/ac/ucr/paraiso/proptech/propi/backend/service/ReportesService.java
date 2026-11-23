package cr.ac.ucr.paraiso.proptech.propi.backend.service;

import cr.ac.ucr.paraiso.proptech.propi.backend.dto.ReporteAuditoriaDTO;
import cr.ac.ucr.paraiso.proptech.propi.backend.dto.ReporteClienteDTO;
import cr.ac.ucr.paraiso.proptech.propi.backend.dto.ReporteFacturaDTO;
import cr.ac.ucr.paraiso.proptech.propi.backend.dto.ReporteSeguridadDTO;
import cr.ac.ucr.paraiso.proptech.propi.backend.exception.BusinessException;
import cr.ac.ucr.paraiso.proptech.propi.backend.repository.ReportesRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

// Valida los filtros de cada reporte antes de llamar a los procedimientos almacenados
@Service
public class ReportesService {

    private final ReportesRepository repository;

    public ReportesService(ReportesRepository repository) {
        this.repository = repository;
    }

    public List<ReporteClienteDTO> reporteClientes(LocalDate desde, LocalDate hasta, String estado) {
        validarFechas(desde, hasta);
        // Si no viene estado se muestran todos (T)
        String estadoFinal = (estado == null || estado.isBlank()) ? "T" : estado.trim().toUpperCase();
        if (!estadoFinal.equals("A") && !estadoFinal.equals("I") && !estadoFinal.equals("T")) {
            throw new BusinessException("El estado debe ser A (activo), I (inactivo) o T (todos).");
        }
        return repository.reporteClientes(desde, hasta, estadoFinal);
    }

    public List<ReporteFacturaDTO> reporteFacturas(LocalDate desde, LocalDate hasta) {
        validarFechas(desde, hasta);
        return repository.reporteFacturas(desde, hasta);
    }

    public List<ReporteAuditoriaDTO> reporteAuditoria(LocalDate desde, LocalDate hasta, String usuario) {
        validarFechas(desde, hasta);
        return repository.reporteAuditoria(desde, hasta, limpiarUsuario(usuario));
    }

    public List<ReporteSeguridadDTO> reporteSeguridad(String usuario) {
        return repository.reporteSeguridad(limpiarUsuario(usuario));
    }

    private void validarFechas(LocalDate desde, LocalDate hasta) {
        if (desde == null || hasta == null) {
            throw new BusinessException("Debe indicar la fecha desde y la fecha hasta.");
        }
        if (desde.isAfter(hasta)) {
            throw new BusinessException("La fecha desde no puede ser mayor que la fecha hasta.");
        }
    }

    // Un usuario vacío significa "todos", y el SP lo recibe como NULL
    private String limpiarUsuario(String usuario) {
        if (usuario == null || usuario.isBlank()) {
            return null;
        }
        if (usuario.length() > 128) {
            throw new BusinessException("El usuario no puede superar 128 caracteres.");
        }
        return usuario.trim();
    }
}
