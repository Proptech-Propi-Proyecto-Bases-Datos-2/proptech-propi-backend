package cr.ac.ucr.paraiso.proptech.propi.backend.controller;

import cr.ac.ucr.paraiso.proptech.propi.backend.entity.FacturasDetalle;
import cr.ac.ucr.paraiso.proptech.propi.backend.service.FacturasDetalleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/facturas-detalle")
@CrossOrigin(origins = "http://localhost:4200")
public class FacturasDetalleRestController {
    private final FacturasDetalleService service;

    public FacturasDetalleRestController(FacturasDetalleService service) { 
        this.service = service; 
    }

    @GetMapping 
    public List<FacturasDetalle> findAll() { 
        return service.findAll(); 
    }

    @GetMapping("/{id}") 
    public ResponseEntity<FacturasDetalle> findById(@PathVariable int id) {
        FacturasDetalle value = service.findById(id);
        return value == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(value);
    }
    @PostMapping 
    public FacturasDetalle save(@RequestBody FacturasDetalle value) { 
        return service.save(value); 
    }

    @PostMapping("/insertarFacturaDetalle") 
    public ResponseEntity<Void> insertFacturaDetalle(@RequestBody FacturasDetalle value) {
        service.insertUsingProcedure(value); return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}") 
    public ResponseEntity<FacturasDetalle> update(@PathVariable int id, @RequestBody FacturasDetalle value) {
        FacturasDetalle updated = service.update(id, value);
        return updated == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable int id) {
        if (service.findById(id) == null) return ResponseEntity.notFound().build();
        service.deleteById(id); return ResponseEntity.noContent().build();
    }
}
