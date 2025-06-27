package co.com.bancolombia.usecase.processstats;

import co.com.bancolombia.model.events.gateways.EventsGateway;
import co.com.bancolombia.model.stats.StatsValidatedEvent;
import co.com.bancolombia.model.stats.UserStats;
import co.com.bancolombia.model.stats.gateways.HashValidator;
import co.com.bancolombia.model.stats.gateways.StatsRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ProcessStatsUseCase {

    private final StatsRepository repository;
    private final EventsGateway publisher;
    private final HashValidator hashValidator;

    public Mono<UserStats> processStats(UserStats stats) {
        if (!hashValidator.isValid(stats)) {
            return Mono.error(new IllegalArgumentException("Hash inválido"));
        }

        return repository.save(stats)
                .flatMap(saved -> {
                    StatsValidatedEvent event = mapToEvent(saved);
                    return publisher.emit(event).thenReturn(saved);
                });
    }

    private StatsValidatedEvent mapToEvent(UserStats stats) {
        return new StatsValidatedEvent(
                stats.getTotalContactoClientes(),
                stats.getMotivoReclamo(),
                stats.getMotivoGarantia(),
                stats.getMotivoDuda(),
                stats.getMotivoCompra(),
                stats.getMotivoFelicitaciones(),
                stats.getMotivoCambio(),
                stats.getTimestamp()
        );
    }

}
