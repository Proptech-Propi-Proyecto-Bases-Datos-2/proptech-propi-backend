package cr.ac.ucr.paraiso.proptech.propi.backend.service;

import cr.ac.ucr.paraiso.proptech.propi.backend.entity.FacturasDetalle;
import cr.ac.ucr.paraiso.proptech.propi.backend.repository.FacturasDetalleRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import cr.ac.ucr.paraiso.proptech.propi.backend.exception.BusinessException;
import cr.ac.ucr.paraiso.proptech.propi.backend.exception.ResourceNotFoundException;

@Service
@Transactional
public class FacturasDetalleService {
    private final FacturasDetalleRepository repository;

    public FacturasDetalleService(FacturasDetalleRepository repository) { 
        this.repository = repository; 
    }

    public List<FacturasDetalle> findAll() { return repository.findAll(); }
    public FacturasDetalle findById(int id) { return repository.findById(id).orElse(null); }
    public FacturasDetalle save(FacturasDetalle facturaDetalle) {
        validar(facturaDetalle);
        return repository.save(facturaDetalle);
    }

    public void insertUsingProcedure(FacturasDetalle facturaDetalle) {
        validar(facturaDetalle);
        repository.insertFacturaDetalle(
                facturaDetalle.getFactura().getIdFactura(),
                facturaDetalle.getProductoServicio().getIdProducto(),
                facturaDetalle.getCantidad(),
                facturaDetalle.getMontoLinea());
    }
    
    public FacturasDetalle update(int id, FacturasDetalle facturaDetalle) {
        ValidationUtils.id(id, "idDetalle");
        validar(facturaDetalle);
        if (facturaDetalle.getIdDetalle() != id) throw new BusinessException("El id de la URL no coincide con el cuerpo.");
        if (!repository.existsById(id)) throw new ResourceNotFoundException("Detalle no encontrado con el ID: " + id);

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
    public void deleteById(int id) {
        ValidationUtils.id(id, "idDetalle");
        if (!repository.existsById(id)) throw new ResourceNotFoundException("Detalle no encontrado con el ID: " + id);
        repository.deleteById(id);
    }
    private void validar(FacturasDetalle value) {
        if (value == null) throw new BusinessException("El cuerpo de la solicitud es obligatorio.");
        if (value.getFactura() == null || value.getProductoServicio() == null) {
            throw new BusinessException("factura y productoServicio son obligatorios.");
        }
        ValidationUtils.id(value.getFactura().getIdFactura(), "idFactura");
        ValidationUtils.id(value.getProductoServicio().getIdProducto(), "idProducto");
        if (value.getCantidad() <= 0) throw new BusinessException("cantidad debe ser mayor que cero.");
        ValidationUtils.positive(value.getMontoLinea(), "montoLinea");
    }
}
