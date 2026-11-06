package cr.ac.ucr.paraiso.proptech.propi.backend.controller;

import cr.ac.ucr.paraiso.proptech.propi.backend.entity.NotasCreditoDebito;
import cr.ac.ucr.paraiso.proptech.propi.backend.service.NotasCreditoDebitoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notas-credito-debito")
@CrossOrigin(origins = "http://localhost:4200")
public class NotasCreditoDebitoRestController {
    private final NotasCreditoDebitoService service;

    public NotasCreditoDebitoRestController(NotasCreditoDebitoService service) { 
        this.service = service; 
    }

    @GetMapping 
    public List<NotasCreditoDebito> findAll() { 
        return service.findAll(); 
    }

    @GetMapping("/{id}") 
    public ResponseEntity<NotasCreditoDebito> findById(@PathVariable int id) {
        NotasCreditoDebito value = service.findById(id);
        return value == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(value);
    }

    @PostMapping 
    public NotasCreditoDebito save(@RequestBody NotasCreditoDebito value) { 
        return service.save(value); 
    }

    @PostMapping("/insertarNota")
    public ResponseEntity<Void> insertarNota(@RequestBody NotasCreditoDebito value) {
        service.insertUsingProcedure(value); return ResponseEntity.ok().build();
    }
    @PutMapping("/{id}") public ResponseEntity<NotasCreditoDebito> update(@PathVariable int id, @RequestBody NotasCreditoDebito value) {
        NotasCreditoDebito updated = service.update(id, value);
        return updated == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(updated);
    }
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable int id) {
        if (service.findById(id) == null) return ResponseEntity.notFound().build();
        service.deleteById(id); return ResponseEntity.noContent().build();
    }
}
