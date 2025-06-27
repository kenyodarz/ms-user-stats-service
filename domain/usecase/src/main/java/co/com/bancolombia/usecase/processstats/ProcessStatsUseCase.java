package co.com.bancolombia.usecase.processstats;

import co.com.bancolombia.model.events.gateways.EventsGateway;
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

    public Mono<Void> processStats(UserStats stats) {
        if (!hashValidator.isValid(stats)) {
            return Mono.error(new IllegalArgumentException("Hash inválido"));
        }

        return repository.save(stats)
                .then(publisher.emit(stats));
    }

}
