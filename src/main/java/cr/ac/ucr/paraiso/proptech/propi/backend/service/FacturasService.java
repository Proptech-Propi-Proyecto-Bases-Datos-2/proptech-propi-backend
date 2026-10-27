package cr.ac.ucr.paraiso.proptech.propi.backend.service;

import cr.ac.ucr.paraiso.proptech.propi.backend.entity.Facturas;
import cr.ac.ucr.paraiso.proptech.propi.backend.repository.FacturasRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Service
@Transactional
public class FacturasService {
    private final FacturasRepository repository;

    public FacturasService(FacturasRepository repository) { 
        this.repository = repository; 
    }


    public List<Facturas> findAll() { return repository.findAll(); }
    public Facturas findById(int id) { return repository.findById(id).orElse(null); }
    public Facturas save(Facturas factura) { return repository.save(factura); }
    public Facturas update(int id, Facturas factura) {
        if (factura.getIdFactura() != id || !repository.existsById(id)) return null;
        

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

    public void deleteById(int id) { repository.deleteById(id); }

    public void insertUsingProcedure(Facturas factura) {
        repository.insertFactura(factura.getCliente().getIdCliente(),
                factura.getFechaFactura() == null ? LocalDateTime.now() : factura.getFechaFactura(),
                factura.getCondicionVenta(), factura.getMedioPago(),
                factura.getMoneda() == null ? "CRC" : factura.getMoneda(),
                factura.getTipoCambio() == null ? new BigDecimal("1.0000") : factura.getTipoCambio(),
                factura.getObservaciones(), factura.getSubtotal(),
                factura.getTotalImpuestos(), factura.getMontoTotal());
    }
}
