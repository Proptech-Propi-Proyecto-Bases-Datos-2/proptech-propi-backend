package cr.ac.ucr.paraiso.proptech.propi.backend.service;

import cr.ac.ucr.paraiso.proptech.propi.backend.entity.TiposImpuesto;
import cr.ac.ucr.paraiso.proptech.propi.backend.repository.TiposImpuestoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TiposImpuestoService {
    private final TiposImpuestoRepository repository;

    public TiposImpuestoService(TiposImpuestoRepository repository) { this.repository = repository; }
    public List<TiposImpuesto> findAll() { return repository.findAll(); }
    public TiposImpuesto findById(int id) { return repository.findById(id).orElse(null); }
    public TiposImpuesto save(TiposImpuesto tipo) { return repository.save(tipo); }
    public TiposImpuesto update(int id, TiposImpuesto tipo) {
        if (tipo.getIdImpuesto() != id || !repository.existsById(id)) return null;
        
        Optional<TiposImpuesto> resultado = repository.findById(id);

        if (resultado.isEmpty()) {
            return null;
        }

        TiposImpuesto tipoEncontrado = resultado.get();

        tipoEncontrado.setTarifa(tipo.getTarifa());
        tipoEncontrado.setDescripcion(tipo.getDescripcion());
        tipoEncontrado.setPorcentaje(tipo.getPorcentaje());
        tipoEncontrado.setEstado(tipo.getEstado());

        return repository.save(tipoEncontrado);
    }

    public void deleteById(int id) { repository.deleteById(id); }
    
    public void insertUsingProcedure(TiposImpuesto tipo) {
        repository.insertTipoImpuesto(tipo.getTarifa(), tipo.getDescripcion(),
                tipo.getPorcentaje(), tipo.getEstado() == null ? "A" : tipo.getEstado());
    }
}
