package cr.ac.ucr.paraiso.proptech.propi.backend.controller;

import cr.ac.ucr.paraiso.proptech.propi.backend.entity.Facturas;
import cr.ac.ucr.paraiso.proptech.propi.backend.service.FacturasService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/facturas")
@CrossOrigin(origins = "http://localhost:4200")
public class FacturasRestController {
    private final FacturasService service;

    public FacturasRestController(FacturasService service) { 
        this.service = service; 
    }
    @GetMapping 
    public List<Facturas> findAll() { 
        return service.findAll(); 
    }

    @GetMapping("/{id}") 
    public ResponseEntity<Facturas> findById(@PathVariable int id) {
        Facturas value = service.findById(id);
        return value == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(value);
    }

    @PostMapping 
    public Facturas save(@RequestBody Facturas value) { 
        return service.save(value); 
    }

    @PutMapping("/{id}") public ResponseEntity<Facturas> update(@PathVariable int id, @RequestBody Facturas value) {
        Facturas updated = service.update(id, value);
        return updated == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(updated);
    }
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable int id) {
        if (service.findById(id) == null) return ResponseEntity.notFound().build();
        service.deleteById(id); return ResponseEntity.noContent().build();
    }
}
