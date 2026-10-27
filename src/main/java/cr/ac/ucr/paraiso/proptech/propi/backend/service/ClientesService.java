package cr.ac.ucr.paraiso.proptech.propi.backend.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import cr.ac.ucr.paraiso.proptech.propi.backend.dto.ClienteDTO;
import cr.ac.ucr.paraiso.proptech.propi.backend.entity.Clientes;
import cr.ac.ucr.paraiso.proptech.propi.backend.repository.ClientesRepository;
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

        int idCliente = clienteRepository.insertClient(
                cliente.getNombre(),
                cliente.getNumIdentificacion(),
                cliente.getTipoIdentificacion(),
                cliente.getProvincia(),
                cliente.getCanton(),
                cliente.getDistrito(),
                cliente.getBarrio(),
                cliente.getProfesion(),
                cliente.getActividadEconomica(),
                cliente.getCorreoElectronico(),
                cliente.getTelefono());
        return idCliente;
    }

    public Clientes save(Clientes cliente) {
        return clienteRepository.save(cliente);
    }

    public List<Clientes> findAll() {
        return clienteRepository.findAll();
    }

    public List<ClienteDTO> obtenerClientes(String estado) {
        // Si no se envía estado o viene vacío, se pasa null para consultar todos
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
                dto.setProfesion((String) fila[8]);
            if (fila[9] != null)
                dto.setActividadEconomica((String) fila[9]);
            if (fila[10] != null)
                dto.setCorreoElectronico((String) fila[10]);
            if (fila[11] != null)
                dto.setTelefono((String) fila[11]);
            if (fila[12] != null) {
                if (fila[12] instanceof LocalDateTime) {
                    dto.setFechaRegistro((LocalDateTime) fila[12]);
                } else if (fila[12] instanceof Timestamp) {
                    dto.setFechaRegistro(((Timestamp) fila[12]).toLocalDateTime());
                }
            }
            // Mapeo seguro para el campo CHAR(1) 'estado'
            if (fila[13] != null) {
                dto.setEstado(String.valueOf(fila[13]));
            }
            ;

            listaClientes.add(dto);
        }

        return listaClientes;
    }

    public ClienteDTO findById(int idCliente) {
        // 1. Reutiliza la llamada al SP pasando null para que consulte los registros
        List<ClienteDTO> todosLosClientes = obtenerClientes(null);

        // 2. Filtra por idCliente usando Java Stream
        return todosLosClientes.stream()
                .filter(c -> c.getIdCliente() != 0 && c.getIdCliente()==idCliente)
                .findFirst()
                .orElse(null); // Retorna null si no encuentra el ID
    }

    public void deleteById(int idCliente) {
        clienteRepository.deleteById(idCliente);
    }

    public void delete(Clientes cliente) {
        clienteRepository.delete(cliente);
    }

    public ClienteDTO update(int id, ClienteDTO dto) {
        // 1. Verificar si el cliente existe usando tu método findById
        ClienteDTO clienteExistente = findById(id);
        if (clienteExistente == null) {
            return null; // Retorna null si el ID no se encuentra en la BD
        }

        // 2. Ejecutar el Stored Procedure de actualización a través del repositorio
        // SQL Server se encarga de recibir los String y convertirlos a byte[] (cifrados)
        clienteRepository.actualizarClienteSP(
            id,
            dto.getNombre(),
            dto.getNumIdentificacion(),
            dto.getTipoIdentificacion(),
            dto.getProvincia(),
            dto.getCanton(),
            dto.getDistrito(),
            dto.getBarrio(),
            dto.getProfesion(),
            dto.getActividadEconomica(),
            dto.getCorreoElectronico(),
            dto.getTelefono(),
            dto.getEstado() != null ? dto.getEstado() : "A"
        );

        // 3. Retornar el cliente consultado nuevamente con sus datos descifrados
        return findById(id);
    }

}
