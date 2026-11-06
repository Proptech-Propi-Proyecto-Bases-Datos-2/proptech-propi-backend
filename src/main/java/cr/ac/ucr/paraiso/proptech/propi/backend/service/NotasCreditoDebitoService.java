package cr.ac.ucr.paraiso.proptech.propi.backend.service;

import cr.ac.ucr.paraiso.proptech.propi.backend.entity.NotasCreditoDebito;
import cr.ac.ucr.paraiso.proptech.propi.backend.repository.NotasCreditoDebitoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import cr.ac.ucr.paraiso.proptech.propi.backend.exception.BusinessException;
import cr.ac.ucr.paraiso.proptech.propi.backend.exception.ResourceNotFoundException;

@Service
@Transactional
public class NotasCreditoDebitoService {
    private final NotasCreditoDebitoRepository repository;

    public NotasCreditoDebitoService(NotasCreditoDebitoRepository repository) { this.repository = repository; }
    public List<NotasCreditoDebito> findAll() { return repository.findAll(); }
    public NotasCreditoDebito findById(int id) { return repository.findById(id).orElse(null); }
    public NotasCreditoDebito save(NotasCreditoDebito entity) {
        validar(entity);
        return repository.save(entity);
    }
    
    public NotasCreditoDebito update(int id, NotasCreditoDebito notas) {
        ValidationUtils.id(id, "idNota");
        validar(notas);
        if (notas.getIdNota() != id) throw new BusinessException("El id de la URL no coincide con el cuerpo.");
        if (!repository.existsById(id)) throw new ResourceNotFoundException("Nota no encontrada con el ID: " + id);
        
        Optional<NotasCreditoDebito> resultado = repository.findById(id);

        if (resultado.isEmpty()) {
            return null;
        }

        NotasCreditoDebito notasEncontrada = resultado.get();

        notasEncontrada.setFactura(notas.getFactura());
        notasEncontrada.setFecha(notas.getFecha());
        notasEncontrada.setTipo(notas.getTipo());
        notasEncontrada.setMotivo(notas.getMotivo());
        notasEncontrada.setMontoTotal(notas.getMontoTotal());

        return repository.save(notasEncontrada);
    }


    public void deleteById(int id) {
        ValidationUtils.id(id, "idNota");
        if (!repository.existsById(id)) throw new ResourceNotFoundException("Nota no encontrada con el ID: " + id);
        repository.deleteById(id);
    }
    
    public void insertUsingProcedure(NotasCreditoDebito notas) {
        validar(notas);
        repository.insertNota(notas.getFactura().getIdFactura(),
                notas.getTipo(), notas.getMotivo(), notas.getMontoTotal());
    }

    private void validar(NotasCreditoDebito value) {
        if (value == null) throw new BusinessException("El cuerpo de la solicitud es obligatorio.");
        if (value.getFactura() == null) throw new BusinessException("factura es obligatoria.");
        ValidationUtils.id(value.getFactura().getIdFactura(), "idFactura");
        if (!"Credito".equals(value.getTipo()) && !"Debito".equals(value.getTipo())) {
            throw new BusinessException("tipo debe ser Credito o Debito.");
        }
        ValidationUtils.length(value.getMotivo(), "motivo", 255);
        ValidationUtils.positive(value.getMontoTotal(), "montoTotal");
    }
}
