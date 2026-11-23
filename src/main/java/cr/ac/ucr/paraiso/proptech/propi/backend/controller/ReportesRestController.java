package cr.ac.ucr.paraiso.proptech.propi.backend.controller;

import cr.ac.ucr.paraiso.proptech.propi.backend.dto.ReporteAuditoriaDTO;
import cr.ac.ucr.paraiso.proptech.propi.backend.dto.ReporteClienteDTO;
import cr.ac.ucr.paraiso.proptech.propi.backend.dto.ReporteFacturaDTO;
import cr.ac.ucr.paraiso.proptech.propi.backend.dto.ReporteSeguridadDTO;
import cr.ac.ucr.paraiso.proptech.propi.backend.service.ReportesService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

// Endpoints de los 4 reportes del REQ 007. Las fechas llegan en formato yyyy-MM-dd.
@RestController
@RequestMapping("/api/reportes")
@CrossOrigin(origins = "http://localhost:4200")
public class ReportesRestController {

    private final ReportesService service;

    public ReportesRestController(ReportesService service) {
        this.service = service;
    }

    // GET /api/reportes/clientes?desde=2026-01-01&hasta=2026-12-31&estado=T
    @GetMapping("/clientes")
    public List<ReporteClienteDTO> reporteClientes(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @RequestParam(required = false) String estado) {
        return service.reporteClientes(desde, hasta, estado);
    }

    // GET /api/reportes/facturas?desde=2026-01-01&hasta=2026-12-31
    @GetMapping("/facturas")
    public List<ReporteFacturaDTO> reporteFacturas(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return service.reporteFacturas(desde, hasta);
    }

    // GET /api/reportes/auditoria?desde=2026-01-01&hasta=2026-12-31&usuario=login_digitador
    @GetMapping("/auditoria")
    public List<ReporteAuditoriaDTO> reporteAuditoria(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @RequestParam(required = false) String usuario) {
        return service.reporteAuditoria(desde, hasta, usuario);
    }

    // GET /api/reportes/seguridad?usuario=user_digitador
    @GetMapping("/seguridad")
    public List<ReporteSeguridadDTO> reporteSeguridad(@RequestParam(required = false) String usuario) {
        return service.reporteSeguridad(usuario);
    }
}
