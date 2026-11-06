package cr.ac.ucr.paraiso.proptech.propi.backend.service;

import cr.ac.ucr.paraiso.proptech.propi.backend.entity.Facturas;
import cr.ac.ucr.paraiso.proptech.propi.backend.repository.FacturasRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import cr.ac.ucr.paraiso.proptech.propi.backend.exception.BusinessException;
import cr.ac.ucr.paraiso.proptech.propi.backend.exception.ResourceNotFoundException;

@Service
@Transactional
public class FacturasService {
    private final FacturasRepository repository;

    public FacturasService(FacturasRepository repository) { 
        this.repository = repository; 
    }


    public List<Facturas> findAll() { return repository.findAll(); }
    public Facturas findById(int id) { return repository.findById(id).orElse(null); }
    public Facturas save(Facturas factura) {
        validar(factura);
        return repository.save(factura);
    }
    public Facturas update(int id, Facturas factura) {
        ValidationUtils.id(id, "idFactura");
        validar(factura);
        if (factura.getIdFactura() != id) throw new BusinessException("El id de la URL no coincide con el cuerpo.");
        if (!repository.existsById(id)) throw new ResourceNotFoundException("Factura no encontrada con el ID: " + id);
        

        Optional<Facturas> resultado = repository.findById(id);

        if (resultado.isEmpty()) {
            return null;
        }

        Facturas facturaEncontrada = resultado.get();

        facturaEncontrada.setCliente(factura.getCliente());
        facturaEncontrada.setFechaFactura(factura.getFechaFactura());
        facturaEncontrada.setCondicionVenta(factura.getCondicionVenta());
        facturaEncontrada.setMedioPago(factura.getMedioPago());
        facturaEncontrada.setMoneda(factura.getMoneda());
        facturaEncontrada.setTipoCambio(factura.getTipoCambio());
        facturaEncontrada.setObservaciones(factura.getObservaciones());
        facturaEncontrada.setSubtotal(factura.getSubtotal());
        facturaEncontrada.setTotalImpuestos(factura.getTotalImpuestos());
        facturaEncontrada.setMontoTotal(factura.getMontoTotal());

        return repository.save(facturaEncontrada);
    }    

    public void deleteById(int id) {
        ValidationUtils.id(id, "idFactura");
        if (!repository.existsById(id)) throw new ResourceNotFoundException("Factura no encontrada con el ID: " + id);
        repository.deleteById(id);
    }

    private void validar(Facturas value) {
        if (value == null) throw new BusinessException("El cuerpo de la solicitud es obligatorio.");
        if (value.getCliente() == null) throw new BusinessException("cliente es obligatorio.");
        ValidationUtils.id(value.getCliente().getIdCliente(), "idCliente");
        ValidationUtils.length(value.getCondicionVenta(), "condicionVenta", 30);
        ValidationUtils.length(value.getMedioPago(), "medioPago", 30);
        ValidationUtils.length(value.getMoneda(), "moneda", 3);
        ValidationUtils.decimalNonNegative(value.getTipoCambio(), "tipoCambio");
        ValidationUtils.decimalNonNegative(value.getSubtotal(), "subtotal");
        ValidationUtils.decimalNonNegative(value.getTotalImpuestos(), "totalImpuestos");
        ValidationUtils.positive(value.getMontoTotal(), "montoTotal");
    }
}
