package cr.ac.ucr.paraiso.proptech.propi.backend.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import cr.ac.ucr.paraiso.proptech.propi.backend.dto.ClienteDTO;
import cr.ac.ucr.paraiso.proptech.propi.backend.entity.Clientes;
import cr.ac.ucr.paraiso.proptech.propi.backend.repository.ClientesRepository;
import cr.ac.ucr.paraiso.proptech.propi.backend.exception.BusinessException;
import cr.ac.ucr.paraiso.proptech.propi.backend.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ClientesService {

    private final ClientesRepository clienteRepository;

    public ClientesService(ClientesRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public int insertarCliente(ClienteDTO cliente) {
        validar(cliente);

        int idCliente = clienteRepository.insertClient(
                cliente.getNombre(),
                cliente.getNumIdentificacion(),
                cliente.getTipoIdentificacion(),
                cliente.getProvincia(),
                cliente.getCanton(),
                cliente.getDistrito(),
                cliente.getBarrio(),
                cliente.getDireccionExacta(),
                cliente.getProfesion(),
                cliente.getActividadEconomica(),
                cliente.getCorreoElectronico(),
                cliente.getTelefono());
        if (idCliente <= 0) {
            throw new BusinessException("La base de datos no devolvió el identificador del cliente.");
        }
        return idCliente;
    }

    public Clientes save(Clientes cliente) {
        return clienteRepository.save(cliente);
    }

    public List<Clientes> findAll() {
        return clienteRepository.findAll();
    }

    public List<ClienteDTO> obtenerClientes(String estado) {
        ValidationUtils.state(estado);
        String parametroEstado = (estado != null && !estado.trim().isEmpty()) ? estado : null;

        // 1. Obtener la lista de objetos devuelta por el procedimiento almacenado
        List<Object[]> resultados = clienteRepository.consultarClientesNativo(parametroEstado);

        // 2. Transformar los arreglos de objetos en instancias de ClienteDTO usando
        // Setters
        List<ClienteDTO> listaClientes = new ArrayList<>();

        for (Object[] fila : resultados) {
            ClienteDTO dto = new ClienteDTO();

            // Mapeo campo por campo segun el orden del SELECT de tu SP
            if (fila[0] != null)
                dto.setIdCliente((Integer) fila[0]);
            if (fila[1] != null)
                dto.setNombre((String) fila[1]);
            if (fila[2] != null)
                dto.setNumIdentificacion((String) fila[2]);
            if (fila[3] != null)
                dto.setTipoIdentificacion((String) fila[3]);
            if (fila[4] != null)
                dto.setProvincia((String) fila[4]);
            if (fila[5] != null)
                dto.setCanton((String) fila[5]);
            if (fila[6] != null)
                dto.setDistrito((String) fila[6]);
            if (fila[7] != null)
                dto.setBarrio((String) fila[7]);
            if (fila[8] != null)
                dto.setDireccionExacta((String) fila[8]);
            if (fila[9] != null)
                dto.setProfesion((String) fila[9]);
            if (fila[10] != null)
                dto.setActividadEconomica((String) fila[10]);
            if (fila[11] != null)
                dto.setCorreoElectronico((String) fila[11]);
            if (fila[12] != null) {
                dto.setTelefono((String) fila[12]);
            }
            if (fila[13] != null) {
                if (fila[13] instanceof LocalDateTime) {
                    dto.setFechaRegistro((LocalDateTime) fila[13]);
                } else if (fila[13] instanceof Timestamp) {
                    dto.setFechaRegistro(((Timestamp) fila[13]).toLocalDateTime());
                }
            }
            if (fila[14] != null) {
                dto.setEstado(String.valueOf(fila[14]));
            }
            ;

            listaClientes.add(dto);
        }

        return listaClientes;
    }

    public ClienteDTO findById(int idCliente) {
        ValidationUtils.id(idCliente, "idCliente");
        return obtenerClientes(null).stream()
                .filter(c -> c.getIdCliente() == idCliente)
                .findFirst().orElse(null);
    }

    public ClienteDTO findRequiredById(int idCliente) {
        ClienteDTO cliente = findById(idCliente);
        if (cliente == null) {
            throw new ResourceNotFoundException("Cliente no encontrado con el ID: " + idCliente);
        }
        return cliente;
    }

    public void deleteById(int idCliente) {
        desactivar(idCliente);
    }

    public void delete(Clientes cliente) {
        if (cliente == null) {
            throw new BusinessException("El cliente es obligatorio.");
        }
        desactivar(cliente.getIdCliente());
    }

    public ClienteDTO update(int id, ClienteDTO dto) {
        ValidationUtils.id(id, "idCliente");
        validar(dto);
        ClienteDTO clienteExistente = findById(id);
        if (clienteExistente == null) {
            throw new ResourceNotFoundException("Cliente no encontrado con el ID: " + id);
        }

        // 2. Ejecutar el Stored Procedure de actualización a través del repositorio
        // SQL Server se encarga de recibir los String y convertirlos a byte[] (cifrados)
        clienteRepository.actualizarCliente(
            id,
            dto.getNombre(),
            dto.getNumIdentificacion(),
            dto.getTipoIdentificacion(),
            dto.getProvincia(),
            dto.getCanton(),
            dto.getDistrito(),
            dto.getBarrio(),
            dto.getDireccionExacta(),
            dto.getProfesion(),
            dto.getActividadEconomica(),
            dto.getCorreoElectronico(),
            dto.getTelefono(),
            dto.getEstado() != null ? dto.getEstado() : "A"
        );

        return findById(id);
    }

    public void desactivar(int id) {
        ValidationUtils.id(id, "idCliente");
        if (findById(id) == null) {
            throw new ResourceNotFoundException("Cliente no encontrado con el ID: " + id);
        }
        clienteRepository.desactivarCliente(id);
    }

    private void validar(ClienteDTO dto) {
        if (dto == null) throw new BusinessException("El cuerpo de la solicitud es obligatorio.");
        ValidationUtils.required(dto.getNombre(), "nombre");
        ValidationUtils.required(dto.getNumIdentificacion(), "numIdentificacion");
        ValidationUtils.required(dto.getTipoIdentificacion(), "tipoIdentificacion");
        ValidationUtils.required(dto.getProvincia(), "provincia");
        ValidationUtils.required(dto.getCanton(), "canton");
        ValidationUtils.required(dto.getDistrito(), "distrito");
        ValidationUtils.required(dto.getBarrio(), "barrio");
        if (dto.getDireccionExacta() == null || dto.getDireccionExacta().isBlank()) {
            dto.setDireccionExacta("No especificada");
        }
        ValidationUtils.required(dto.getProfesion(), "profesion");
        ValidationUtils.required(dto.getActividadEconomica(), "actividadEconomica");
        ValidationUtils.required(dto.getCorreoElectronico(), "correoElectronico");
        ValidationUtils.required(dto.getTelefono(), "telefono");
        ValidationUtils.length(dto.getNombre(), "nombre", 150);
        ValidationUtils.length(dto.getNumIdentificacion(), "numIdentificacion", 50);
        ValidationUtils.length(dto.getTipoIdentificacion(), "tipoIdentificacion", 20);
        ValidationUtils.length(dto.getProvincia(), "provincia", 40);
        ValidationUtils.length(dto.getCanton(), "canton", 40);
        ValidationUtils.length(dto.getDistrito(), "distrito", 40);
        ValidationUtils.length(dto.getBarrio(), "barrio", 40);
        ValidationUtils.length(dto.getDireccionExacta(), "direccionExacta", 500);
        ValidationUtils.length(dto.getProfesion(), "profesion", 100);
        ValidationUtils.length(dto.getActividadEconomica(), "actividadEconomica", 100);
        ValidationUtils.length(dto.getCorreoElectronico(), "correoElectronico", 256);
        ValidationUtils.length(dto.getTelefono(), "telefono", 20);
        if (!dto.getCorreoElectronico().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new BusinessException("El correoElectronico no tiene un formato válido.");
        }
        if (!dto.getTelefono().matches("^[0-9+()\\- ]{8,20}$")) {
            throw new BusinessException("El telefono no tiene un formato válido.");
        }
        ValidationUtils.state(dto.getEstado());
    }

}
