package cr.ac.ucr.paraiso.proptech.propi.backend.service;

import cr.ac.ucr.paraiso.proptech.propi.backend.entity.NotasCreditoDebito;
import cr.ac.ucr.paraiso.proptech.propi.backend.repository.NotasCreditoDebitoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class NotasCreditoDebitoService {
    private final NotasCreditoDebitoRepository repository;

    public NotasCreditoDebitoService(NotasCreditoDebitoRepository repository) { this.repository = repository; }
    public List<NotasCreditoDebito> findAll() { return repository.findAll(); }
    public NotasCreditoDebito findById(int id) { return repository.findById(id).orElse(null); }
    public NotasCreditoDebito save(NotasCreditoDebito entity) { return repository.save(entity); }
    
    public NotasCreditoDebito update(int id, NotasCreditoDebito notas) {
        if (notas.getIdNota() != id || !repository.existsById(id)) return null;
        
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


    public void deleteById(int id) { repository.deleteById(id); }
    
    public void insertUsingProcedure(NotasCreditoDebito notas) {
        repository.insertNota(notas.getFactura().getIdFactura(),
                notas.getFecha() == null ? LocalDateTime.now() : notas.getFecha(),
                notas.getTipo(), notas.getMotivo(), notas.getMontoTotal());
    }
}
