package cr.ac.ucr.paraiso.proptech.propi.backend.controller;

import cr.ac.ucr.paraiso.proptech.propi.backend.dto.ClienteDTO;
import cr.ac.ucr.paraiso.proptech.propi.backend.entity.Clientes;
import cr.ac.ucr.paraiso.proptech.propi.backend.service.ClientesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "http://localhost:4200")
public class ClientesRestController {
    private final ClientesService clientesService;

    public ClientesRestController(ClientesService clientesService) {
        this.clientesService = clientesService;
    }

    @PostMapping("/insertarCliente")
    public ResponseEntity<Map<String, Object>> insertarCliente(@RequestBody ClienteDTO cliente) {
        int id = clientesService.insertarCliente(cliente);
        ClienteDTO insertado = clientesService.findRequiredById(id);
        return ResponseEntity.ok(Map.of("mensaje", "Cliente insertado correctamente", "cliente", insertado));
    }

    @GetMapping("/cifrados")
    public List<Clientes> listarClientesCifrados() {
        return clientesService.findAll();
    }

    @GetMapping
    public List<ClienteDTO> listarClientes(@RequestParam(required = false) String estado) {
        return clientesService.obtenerClientes(estado);
    }

    @GetMapping("/id/{id}")
    public ClienteDTO getClienteById(@PathVariable int id) {
        return clientesService.findRequiredById(id);
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<Map<String, Object>> updateCliente(@PathVariable int id, @RequestBody ClienteDTO cliente) {
        ClienteDTO actualizado = clientesService.update(id, cliente);
        return ResponseEntity.ok(Map.of("mensaje", "Cliente actualizado correctamente", "cliente", actualizado));
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Map<String, String>> deleteCliente(@PathVariable int id) {
        clientesService.desactivar(id);
        return ResponseEntity.ok(Map.of("mensaje", "Cliente desactivado correctamente"));
    }
}
