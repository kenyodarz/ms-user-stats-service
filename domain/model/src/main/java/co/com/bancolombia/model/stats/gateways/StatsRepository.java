package co.com.bancolombia.model.stats.gateways;

import co.com.bancolombia.model.stats.UserStats;
import reactor.core.publisher.Mono;

public interface StatsRepository {

    Mono<UserStats> save(UserStats stats);

}
