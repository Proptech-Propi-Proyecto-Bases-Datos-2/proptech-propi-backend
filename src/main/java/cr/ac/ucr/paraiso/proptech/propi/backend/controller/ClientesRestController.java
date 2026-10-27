package cr.ac.ucr.paraiso.proptech.propi.backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @PostMapping("/insertarCliente")
    public ResponseEntity<?> insertarCliente(@RequestBody ClienteDTO cliente) {

        try {
            int clienteId = clientesService.insertarCliente(cliente);
            ClienteDTO clienteInsertado = clientesService.findById(clienteId);

            if (clienteInsertado == null) {
                return ResponseEntity.status(500).body("Error al insertar el cliente");
            }

            return ResponseEntity.ok(Map.of(
                "mensaje", "Cliente insertado correctamente",
                "cliente", "ID:" + clienteInsertado.getIdCliente() + " - Nombre: " + clienteInsertado.getNombre()
            ));

        } catch (DataAccessException e) {
            // Aquí cae la excepción cuando el Login de SQL Server no tiene permisos (DENY / REVOKE)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                "error", "Acceso Denegado",
                "detalle", "El usuario de base de datos no tiene permisos para realizar esta operación."
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error", "Error interno",
                "detalle", e.getMessage()
            ));
        }
    }

    
    @GetMapping("/cifrados")
    public ResponseEntity<?> listarClientesCifrados() {
        try {
            List<Clientes> clientes = clientesService.findAll();
            return ResponseEntity.ok(clientes);
        } catch (DataAccessException e) {
            // Aquí cae la excepción cuando el Login de SQL Server no tiene permisos (DENY / REVOKE)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                "error", "Acceso Denegado",
                "detalle", "El usuario de base de datos no tiene permisos para realizar esta operación."
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                        "error", "Error al consultar los clientes descifrados",
                        "detalle", e.getMessage()
                    ));
        }
    }


    // Endpoint GET: /api/clientes O /api/clientes?estado=A
    @GetMapping
    public ResponseEntity<?> listarClientes(@RequestParam(required = false) String estado) {
        try {
            List<ClienteDTO> clientes = clientesService.obtenerClientes(estado);
            return ResponseEntity.ok(clientes);
        } catch (DataAccessException e) {
            // Aquí cae la excepción cuando el Login de SQL Server no tiene permisos (DENY / REVOKE)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                "error", "Acceso Denegado",
                "detalle", "El usuario de base de datos no tiene permisos para realizar esta operación."
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                        "error", "Error al consultar los clientes descifrados",
                        "detalle", e.getMessage()
                    ));
        }
    }

    @GetMapping("/id/{id}")//devuelve una coincidencia de cliente por id
    public ResponseEntity<?> getClienteById(@PathVariable int id) {
        try {
            ClienteDTO cliente = clientesService.findById(id);
            
            if (cliente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", "Cliente no encontrado con el ID: " + id));
            }
            
            return ResponseEntity.ok(cliente);
        } catch (DataAccessException e) {
            // Aquí cae la excepción cuando el Login de SQL Server no tiene permisos (DENY / REVOKE)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                "error", "Acceso Denegado",
                "detalle", "El usuario de base de datos no tiene permisos para realizar esta operación."
            ));    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                        "error", "Error al consultar el cliente descifrado",
                        "detalle", e.getMessage()
                    ));
        }
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<?> updateCliente(@PathVariable int id, @RequestBody ClienteDTO cliente) {

        try {
            ClienteDTO updated = clientesService.update(id, cliente);
            if (updated == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensaje", "Cliente no encontrado con el ID: " + id));
            }
            return ResponseEntity.ok(Map.of(
                "mensaje", "Cliente actualizado correctamente",
                "cliente", "ID:" + updated.getIdCliente() + " - Nombre: " + updated.getNombre()
            ));
        } catch (DataAccessException e) {
            // Aquí cae la excepción cuando el Login de SQL Server no tiene permisos (DENY / REVOKE)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                "error", "Acceso Denegado",
                "detalle", "El usuario de base de datos no tiene permisos para realizar esta operación."
            ));    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                        "error", "Error al actualizar el cliente descifrado",
                        "detalle", e.getMessage()
                    ));
        }    
        
    }

    @DeleteMapping("/id/{id}")//devuelve un mensaje de éxito o error al eliminar el cliente
    public ResponseEntity<?> deleteCliente(@PathVariable int id) {

        try {
            ClienteDTO cliente = clientesService.findById(id);
            if(cliente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", "Cliente no encontrado con el ID: " + id));
            }

            clientesService.deleteById(cliente.getIdCliente());
            return ResponseEntity.ok(Map.of(
                "mensaje", "Cliente eliminado correctamente",
                "cliente", "ID:" + cliente.getIdCliente() + " - Nombre: " + cliente.getNombre()
            ));
        } catch (DataAccessException e) {
            // Aquí cae la excepción cuando el Login de SQL Server no tiene permisos (DENY / REVOKE)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                "error", "Acceso Denegado",
                "detalle", "El usuario de base de datos no tiene permisos para realizar esta operación."
            ));    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                        "error", "Error al eliminar el cliente descifrado",
                        "detalle", e.getMessage()
                    ));
        }
    }

}
    