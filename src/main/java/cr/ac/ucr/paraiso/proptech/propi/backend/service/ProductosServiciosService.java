package cr.ac.ucr.paraiso.proptech.propi.backend.service;

import cr.ac.ucr.paraiso.proptech.propi.backend.entity.ProductosServicios;
import cr.ac.ucr.paraiso.proptech.propi.backend.repository.ProductosServiciosRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductosServiciosService {
    private final ProductosServiciosRepository repository;

    public ProductosServiciosService(ProductosServiciosRepository repository) { this.repository = repository; }
    public List<ProductosServicios> findAll() { return repository.findAll(); }
    public ProductosServicios findById(int id) { return repository.findById(id).orElse(null); }
    public ProductosServicios save(ProductosServicios productoServicio) { return repository.save(productoServicio); }
    public ProductosServicios update(int id, ProductosServicios productoServicio) {
        if (productoServicio.getIdProducto() != id || !repository.existsById(id)) return null;
        
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

        return repository.save(productoEncontrado);
    }
    
    public void deleteById(int id) { repository.deleteById(id); }
    
    public void insertUsingProcedure(ProductosServicios productoServicio) {
        repository.insertProductoServicio(productoServicio.getCodCabys(), productoServicio.getTipoImpuesto().getIdImpuesto(),
                productoServicio.getDescripcion(), productoServicio.getUnidadMedida(), productoServicio.getPrecio(),
                productoServicio.getEstado() == null ? "A" : productoServicio.getEstado());
    }
}
