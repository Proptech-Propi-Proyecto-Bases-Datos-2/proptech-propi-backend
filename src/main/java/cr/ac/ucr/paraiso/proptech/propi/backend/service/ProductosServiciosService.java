package cr.ac.ucr.paraiso.proptech.propi.backend.service;

import cr.ac.ucr.paraiso.proptech.propi.backend.entity.ProductosServicios;
import cr.ac.ucr.paraiso.proptech.propi.backend.repository.ProductosServiciosRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import cr.ac.ucr.paraiso.proptech.propi.backend.exception.BusinessException;
import cr.ac.ucr.paraiso.proptech.propi.backend.exception.ResourceNotFoundException;

@Service
@Transactional
public class ProductosServiciosService {
    private final ProductosServiciosRepository repository;

    public ProductosServiciosService(ProductosServiciosRepository repository) { this.repository = repository; }
    public List<ProductosServicios> findAll() { return repository.findAll(); }
    public ProductosServicios findById(int id) { return repository.findById(id).orElse(null); }
    public ProductosServicios save(ProductosServicios productoServicio) {
        validar(productoServicio);
        return repository.save(productoServicio);
    }
    public ProductosServicios update(int id, ProductosServicios productoServicio) {
        ValidationUtils.id(id, "idProducto");
        validar(productoServicio);
        if (productoServicio.getIdProducto() != id) throw new BusinessException("El id de la URL no coincide con el cuerpo.");
        if (!repository.existsById(id)) throw new ResourceNotFoundException("Producto o servicio no encontrado con el ID: " + id);
        
        Optional<ProductosServicios> resultado = repository.findById(id);

        if (resultado.isEmpty()) {
            return null;
        }

        ProductosServicios productoEncontrado = resultado.get();

        productoEncontrado.setCodCabys(productoServicio.getCodCabys());
        productoEncontrado.setTipoImpuesto(productoServicio.getTipoImpuesto());
        productoEncontrado.setDescripcion(productoServicio.getDescripcion());
        productoEncontrado.setUnidadMedida(productoServicio.getUnidadMedida());
        productoEncontrado.setPrecio(productoServicio.getPrecio());
        productoEncontrado.setEstado(productoServicio.getEstado());

        repository.actualizarProductoServicio(id, productoServicio.getCodCabys(),
                productoServicio.getTipoImpuesto().getIdImpuesto(), productoServicio.getDescripcion(),
                productoServicio.getUnidadMedida(), productoServicio.getPrecio());
        return findById(id);
    }
    
    public void deleteById(int id) {
        ValidationUtils.id(id, "idProducto");
        if (!repository.existsById(id)) throw new ResourceNotFoundException("Producto o servicio no encontrado con el ID: " + id);
        repository.desactivarProductoServicio(id);
    }
    
    public void insertUsingProcedure(ProductosServicios productoServicio) {
        validar(productoServicio);
        repository.insertProductoServicio(productoServicio.getCodCabys(), productoServicio.getTipoImpuesto().getIdImpuesto(),
                productoServicio.getDescripcion(), productoServicio.getUnidadMedida(), productoServicio.getPrecio());
    }

    private void validar(ProductosServicios value) {
        if (value == null) throw new BusinessException("El cuerpo de la solicitud es obligatorio.");
        String cabys = ValidationUtils.required(value.getCodCabys(), "codCabys");
        if (!cabys.matches("\\d{13}")) throw new BusinessException("codCabys debe contener exactamente 13 dígitos.");
        if (value.getTipoImpuesto() == null) throw new BusinessException("tipoImpuesto es obligatorio.");
        ValidationUtils.id(value.getTipoImpuesto().getIdImpuesto(), "idImpuesto");
        ValidationUtils.length(value.getDescripcion(), "descripcion", 255);
        ValidationUtils.length(value.getUnidadMedida(), "unidadMedida", 20);
        ValidationUtils.decimalNonNegative(value.getPrecio(), "precio");
        ValidationUtils.state(value.getEstado());
    }
}
