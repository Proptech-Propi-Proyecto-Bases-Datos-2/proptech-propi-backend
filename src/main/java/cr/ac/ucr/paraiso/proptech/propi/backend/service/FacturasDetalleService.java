package cr.ac.ucr.paraiso.proptech.propi.backend.service;

import cr.ac.ucr.paraiso.proptech.propi.backend.entity.FacturasDetalle;
import cr.ac.ucr.paraiso.proptech.propi.backend.repository.FacturasDetalleRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FacturasDetalleService {
    private final FacturasDetalleRepository repository;

    public FacturasDetalleService(FacturasDetalleRepository repository) { 
        this.repository = repository; 
    }

    public List<FacturasDetalle> findAll() { return repository.findAll(); }
    public FacturasDetalle findById(int id) { return repository.findById(id).orElse(null); }
    public FacturasDetalle save(FacturasDetalle facturaDetalle) { return repository.save(facturaDetalle); }
    
    public FacturasDetalle update(int id, FacturasDetalle facturaDetalle) {
        if (facturaDetalle.getIdDetalle() != id || !repository.existsById(id)) {
            return null;
        }

        Optional<FacturasDetalle> resultado = repository.findById(id);

        if (resultado.isEmpty()) {
            return null;
        }

        FacturasDetalle facturaDetalleEncontrada = resultado.get();

        facturaDetalleEncontrada.setFactura(facturaDetalle.getFactura());
        facturaDetalleEncontrada.setProductoServicio(facturaDetalle.getProductoServicio());
        facturaDetalleEncontrada.setCantidad(facturaDetalle.getCantidad());
        facturaDetalleEncontrada.setMontoLinea(facturaDetalle.getMontoLinea());

        return repository.save(facturaDetalleEncontrada);
    }
    public void deleteById(int id) { repository.deleteById(id); }
    public void insertUsingProcedure(FacturasDetalle facturaDetalle) {
        repository.insertFacturaDetalle(facturaDetalle.getFactura().getIdFactura(),
                facturaDetalle.getProductoServicio().getIdProducto(), facturaDetalle.getCantidad(), facturaDetalle.getMontoLinea());
    }
}
