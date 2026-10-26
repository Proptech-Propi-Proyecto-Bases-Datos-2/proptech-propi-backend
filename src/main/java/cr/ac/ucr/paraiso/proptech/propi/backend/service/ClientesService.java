package cr.ac.ucr.paraiso.proptech.propi.backend.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cr.ac.ucr.paraiso.proptech.propi.backend.dto.ClienteDTO;
import cr.ac.ucr.paraiso.proptech.propi.backend.entity.Clientes;
import cr.ac.ucr.paraiso.proptech.propi.backend.entity.Facturas;
import cr.ac.ucr.paraiso.proptech.propi.backend.repository.ClientesRepository;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jakarta.transaction.Transactional;

@Service 
@Transactional 
public class ClientesService {

    private final ClientesRepository clienteRepository;

    public ClientesService(ClientesRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public void insertarCliente(ClienteDTO cliente) {

        clienteRepository.insertClient(
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
                cliente.getTelefono()
        );
    }

    public List<Clientes> findAll() {
        return clienteRepository.findAll();
    }

    public Clientes findById(int idCliente) {
        return clienteRepository.findById(idCliente).orElse(null);
    }

    public void deleteById(int idCliente) {
        clienteRepository.deleteById(idCliente);
    }

    public void delete(Clientes cliente) {
        clienteRepository.delete(cliente);
    }

    public Clientes update(int id, Clientes cliente) {

        if (cliente.getIdCliente() != id) {
            return null;
        }

        Optional<Clientes> resultado = clienteRepository.findById(id);

        if (resultado.isEmpty()) {
            return null;
        }

        Clientes clienteEncontrado = resultado.get();

        clienteEncontrado.setNombre(cliente.getNombre());
        clienteEncontrado.setNumIdentificacion(cliente.getNumIdentificacion());
        clienteEncontrado.setTipoIdentificacion(cliente.getTipoIdentificacion());
        clienteEncontrado.setProvincia(cliente.getProvincia());
        clienteEncontrado.setCanton(cliente.getCanton());
        clienteEncontrado.setDistrito(cliente.getDistrito());
        clienteEncontrado.setBarrio(cliente.getBarrio());
        clienteEncontrado.setProfesion(cliente.getProfesion());
        clienteEncontrado.setActividadEconomica(cliente.getActividadEconomica());
        clienteEncontrado.setCorreoElectronico(cliente.getCorreoElectronico());
        clienteEncontrado.setTelefono(cliente.getTelefono());

        return clienteRepository.save(clienteEncontrado);

    }

}
