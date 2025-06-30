package co.com.bancolombia.dynamodb.util;

import co.com.bancolombia.model.stats.UserStats;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class HashValidatorImplTest {

    private HashValidatorImpl validator;

    @BeforeEach
    void setUp() {
        validator = new HashValidatorImpl();
    }

    @Test
    void shouldReturnTrueWhenHashIsValid() {
        UserStats stats = new UserStats();
        stats.setTotalContactoClientes(100);
        stats.setMotivoReclamo(10);
        stats.setMotivoGarantia(5);
        stats.setMotivoDuda(20);
        stats.setMotivoCompra(30);
        stats.setMotivoFelicitaciones(3);
        stats.setMotivoCambio(2);

        // Generar el hash correcto
        String raw = "100,10,5,20,30,3,2";
        String hash = DigestUtils.md5DigestAsHex(raw.getBytes(StandardCharsets.UTF_8));
        stats.setHash(hash);

        assertTrue(validator.isValid(stats));
    }

    @Test
    void shouldReturnFalseWhenHashIsInvalid() {
        UserStats stats = new UserStats();
        stats.setTotalContactoClientes(100);
        stats.setMotivoReclamo(10);
        stats.setMotivoGarantia(5);
        stats.setMotivoDuda(20);
        stats.setMotivoCompra(30);
        stats.setMotivoFelicitaciones(3);
        stats.setMotivoCambio(2);

        // Hash incorrecto
        stats.setHash("abcdef1234567890abcdef1234567890");

        assertFalse(validator.isValid(stats));
    }
}
