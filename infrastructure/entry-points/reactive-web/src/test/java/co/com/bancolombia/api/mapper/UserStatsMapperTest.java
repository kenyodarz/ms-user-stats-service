package co.com.bancolombia.api.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import co.com.bancolombia.api.request.UserStatsRequest;
import co.com.bancolombia.model.stats.UserStats;
import org.junit.jupiter.api.Test;

class UserStatsMapperTest {

    @Test
    void shouldMapRequestToDomain() {
        // Arrange
        UserStatsRequest request = new UserStatsRequest();
        request.setTotalContactoClientes(100);
        request.setMotivoReclamo(10);
        request.setMotivoGarantia(5);
        request.setMotivoDuda(20);
        request.setMotivoCompra(50);
        request.setMotivoFelicitaciones(8);
        request.setMotivoCambio(7);
        request.setHash("abc123");

        // Act
        UserStats result = UserStatsMapper.toDomain(request);

        // Assert
        assertNotNull(result);
        assertEquals(100, result.getTotalContactoClientes());
        assertEquals(10, result.getMotivoReclamo());
        assertEquals(5, result.getMotivoGarantia());
        assertEquals(20, result.getMotivoDuda());
        assertEquals(50, result.getMotivoCompra());
        assertEquals(8, result.getMotivoFelicitaciones());
        assertEquals(7, result.getMotivoCambio());
        assertEquals("abc123", result.getHash());
    }
}
