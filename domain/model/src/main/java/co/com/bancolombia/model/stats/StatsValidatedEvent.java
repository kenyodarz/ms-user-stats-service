package co.com.bancolombia.model.stats;

public record StatsValidatedEvent(
        Integer totalContactoClientes,
        Integer motivoReclamo,
        Integer motivoGarantia,
        Integer motivoDuda,
        Integer motivoCompra,
        Integer motivoFelicitaciones,
        Integer motivoCambio,
        String timestamp
) {}