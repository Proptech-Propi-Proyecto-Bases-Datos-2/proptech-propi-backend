package cr.ac.ucr.paraiso.proptech.propi.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cr.ac.ucr.paraiso.proptech.propi.backend.dto.ClienteDTO;
import cr.ac.ucr.paraiso.proptech.propi.backend.entity.Clientes;
import cr.ac.ucr.paraiso.proptech.propi.backend.service.ClientesService;


@RestController 
@RequestMapping ("/api/clientes")
@CrossOrigin (origins = "http://localhost:4200")
public class ClientesRestController {
    
    private final ClientesService clientesService;

    public ClientesRestController(ClientesService clientesService) {
        this.clientesService = clientesService;
    }

    @PostMapping("/clientes")
    public ResponseEntity<String> insertarCliente(
            @RequestBody ClienteDTO cliente) {

        clientesService.insertarCliente(cliente);

        return ResponseEntity.ok(cliente.getNombre() + " - insertado correctamente");
    }

    @GetMapping()//devuelve todos los clientes
    public ResponseEntity<List<Clientes>> getAllClientes() {
        List<Clientes> clientes = clientesService.findAll();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/id/{id}")//devuelve una coincidencia de cliente por id
    public ResponseEntity<Clientes> getClienteById(@PathVariable int id) {
        Clientes cliente = clientesService.findById(id);
        if (cliente == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cliente);
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<Clientes> updateCliente(@PathVariable int id, @RequestBody Clientes cliente) {
        Clientes updated = clientesService.update(id, cliente);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/code/{id}")//devuelve un mensaje de éxito o error al eliminar el cliente
    public ResponseEntity<String> deleteResearchProject(@PathVariable String code) {
        Clientes cliente = clientesService.findById(Integer.parseInt(code));
        if(cliente == null) {
            return ResponseEntity.status(500).body("Failed to delete the research project");
        }

        clientesService.delete(cliente);
        return ResponseEntity.ok("Research project deleted successfully");
    }

}
