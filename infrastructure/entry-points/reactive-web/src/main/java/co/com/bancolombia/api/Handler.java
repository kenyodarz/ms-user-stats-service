package co.com.bancolombia.api;

import co.com.bancolombia.model.stats.UserStats;
import co.com.bancolombia.usecase.processstats.ProcessStatsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {

    private final ProcessStatsUseCase useCase;

    public Mono<ServerResponse> handleStats(ServerRequest request) {
        return request.bodyToMono(UserStats.class)
                .flatMap(useCase::processStats)
                .then(ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue("{\"message\": \"Estadística procesada correctamente\"}"))
                .onErrorResume(IllegalArgumentException.class, ex ->
                        ServerResponse.badRequest()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue("{\"error\": \"" + ex.getMessage() + "\"}")
                );
    }
}
