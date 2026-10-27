package cr.ac.ucr.paraiso.proptech.propi.backend.controller;

import cr.ac.ucr.paraiso.proptech.propi.backend.entity.TiposImpuesto;
import cr.ac.ucr.paraiso.proptech.propi.backend.service.TiposImpuestoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tipos-impuestos")
@CrossOrigin(origins = "http://localhost:4200")
public class TiposImpuestoRestController {
    private final TiposImpuestoService service;

    public TiposImpuestoRestController(TiposImpuestoService service) { 
        this.service = service; 
    }

    @GetMapping public List<TiposImpuesto> findAll() { 
        return service.findAll(); 
    }

    @GetMapping("/{id}") 
    public ResponseEntity<TiposImpuesto> findById(@PathVariable int id) {
        TiposImpuesto value = service.findById(id);
        return value == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(value);
    }
    @PostMapping public TiposImpuesto save(@RequestBody TiposImpuesto value) { 
        return service.save(value); 
    }

    @PostMapping("/insertarTipo") public ResponseEntity<Void> insertUsingProcedure(@RequestBody TiposImpuesto value) {
        service.insertUsingProcedure(value); return ResponseEntity.ok().build();
    }
    @PutMapping("/{id}") public ResponseEntity<TiposImpuesto> update(@PathVariable int id, @RequestBody TiposImpuesto value) {
        TiposImpuesto updated = service.update(id, value);
        return updated == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(updated);
    }
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable int id) {
        if (service.findById(id) == null) return ResponseEntity.notFound().build();
        service.deleteById(id); return ResponseEntity.noContent().build();
    }
}
