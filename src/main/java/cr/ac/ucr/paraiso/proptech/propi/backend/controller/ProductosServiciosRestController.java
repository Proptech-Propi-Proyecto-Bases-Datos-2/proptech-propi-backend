package cr.ac.ucr.paraiso.proptech.propi.backend.controller;

import cr.ac.ucr.paraiso.proptech.propi.backend.entity.ProductosServicios;
import cr.ac.ucr.paraiso.proptech.propi.backend.service.ProductosServiciosService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/productos-servicios")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductosServiciosRestController {
    private final ProductosServiciosService service;

    public ProductosServiciosRestController(ProductosServiciosService service) { 
        this.service = service; 
    }
    @GetMapping public List<ProductosServicios> findAll() { 
        return service.findAll(); 
    }
    @GetMapping("/{id}") 
    public ResponseEntity<ProductosServicios> findById(@PathVariable int id) {
        ProductosServicios value = service.findById(id);
        return value == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(value);
    }
    @PostMapping 
    public ProductosServicios save(@RequestBody ProductosServicios value) { 
        return service.save(value); 
    }

    @PostMapping("/insertarProducto") public ResponseEntity<Void> insertarProducto(@RequestBody ProductosServicios value) {
        service.insertUsingProcedure(value); return ResponseEntity.ok().build();
    }
    @PutMapping("/{id}") public ResponseEntity<ProductosServicios> update(@PathVariable int id, @RequestBody ProductosServicios value) {
        ProductosServicios updated = service.update(id, value);
        return updated == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable int id) {
        if (service.findById(id) == null) return ResponseEntity.notFound().build();
        service.deleteById(id); return ResponseEntity.noContent().build();
    }
}
