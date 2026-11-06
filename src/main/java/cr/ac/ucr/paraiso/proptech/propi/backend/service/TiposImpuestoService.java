package cr.ac.ucr.paraiso.proptech.propi.backend.service;

import cr.ac.ucr.paraiso.proptech.propi.backend.entity.TiposImpuesto;
import cr.ac.ucr.paraiso.proptech.propi.backend.repository.TiposImpuestoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import cr.ac.ucr.paraiso.proptech.propi.backend.exception.BusinessException;
import cr.ac.ucr.paraiso.proptech.propi.backend.exception.ResourceNotFoundException;

@Service
@Transactional
public class TiposImpuestoService {
    private final TiposImpuestoRepository repository;

    public TiposImpuestoService(TiposImpuestoRepository repository) { this.repository = repository; }
    public List<TiposImpuesto> findAll() { return repository.findAll(); }
    public TiposImpuesto findById(int id) { return repository.findById(id).orElse(null); }
    public TiposImpuesto save(TiposImpuesto tipo) {
        validar(tipo);
        return repository.save(tipo);
    }
    public TiposImpuesto update(int id, TiposImpuesto tipo) {
        ValidationUtils.id(id, "idImpuesto");
        validar(tipo);
        if (tipo.getIdImpuesto() != id) throw new BusinessException("El id de la URL no coincide con el cuerpo.");
        if (!repository.existsById(id)) throw new ResourceNotFoundException("Tipo de impuesto no encontrado con el ID: " + id);
        
        Optional<TiposImpuesto> resultado = repository.findById(id);

        if (resultado.isEmpty()) {
            return null;
        }

        TiposImpuesto tipoEncontrado = resultado.get();

        tipoEncontrado.setTarifa(tipo.getTarifa());
        tipoEncontrado.setDescripcion(tipo.getDescripcion());
        tipoEncontrado.setPorcentaje(tipo.getPorcentaje());
        tipoEncontrado.setEstado(tipo.getEstado());

        repository.actualizarTipoImpuesto(id, tipo.getTarifa(), tipo.getDescripcion(), tipo.getPorcentaje());
        return findById(id);
    }

    public void deleteById(int id) {
        ValidationUtils.id(id, "idImpuesto");
        if (!repository.existsById(id)) throw new ResourceNotFoundException("Tipo de impuesto no encontrado con el ID: " + id);
        repository.desactivarTipoImpuesto(id);
    }
    
    public void insertUsingProcedure(TiposImpuesto tipo) {
        validar(tipo);
        repository.insertTipoImpuesto(tipo.getTarifa(), tipo.getDescripcion(), tipo.getPorcentaje());
    }

    private void validar(TiposImpuesto tipo) {
        if (tipo == null) throw new BusinessException("El cuerpo de la solicitud es obligatorio.");
        ValidationUtils.required(tipo.getTarifa(), "tarifa");
        ValidationUtils.length(tipo.getTarifa(), "tarifa", 10);
        ValidationUtils.length(tipo.getDescripcion(), "descripcion", 100);
        if (tipo.getPorcentaje() == null || tipo.getPorcentaje().signum() < 0
                || tipo.getPorcentaje().compareTo(new java.math.BigDecimal("100")) > 0) {
            throw new BusinessException("El porcentaje debe estar entre 0 y 100.");
        }
        ValidationUtils.state(tipo.getEstado());
    }
}
