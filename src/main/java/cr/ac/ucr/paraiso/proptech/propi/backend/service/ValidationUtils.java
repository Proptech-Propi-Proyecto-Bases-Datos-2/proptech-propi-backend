package cr.ac.ucr.paraiso.proptech.propi.backend.service;

import cr.ac.ucr.paraiso.proptech.propi.backend.exception.BusinessException;
import java.math.BigDecimal;

final class ValidationUtils {
    private ValidationUtils() {
    }

    static String required(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new BusinessException("El campo " + field + " es obligatorio.");
        }
        return value.trim();
    }

    static void length(String value, String field, int max) {
        if (value != null && value.length() > max) {
            throw new BusinessException("El campo " + field + " no puede superar " + max + " caracteres.");
        }
    }

    static void decimalNonNegative(BigDecimal value, String field) {
        if (value == null || value.signum() < 0) {
            throw new BusinessException("El campo " + field + " debe ser un número mayor o igual a cero.");
        }
    }

    static void positive(BigDecimal value, String field) {
        if (value == null || value.signum() <= 0) {
            throw new BusinessException("El campo " + field + " debe ser mayor que cero.");
        }
    }

    static void id(int id, String field) {
        if (id <= 0) {
            throw new BusinessException("El campo " + field + " debe ser un identificador válido.");
        }
    }

    static void state(String value) {
        if (value != null && !value.equals("A") && !value.equals("I")) {
            throw new BusinessException("El estado debe ser A o I.");
        }
    }
}
