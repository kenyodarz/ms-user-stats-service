package co.com.bancolombia.api;

import co.com.bancolombia.api.mapper.UserStatsMapper;
import co.com.bancolombia.api.request.UserStatsRequest;
import co.com.bancolombia.model.stats.UserStats;
import co.com.bancolombia.usecase.processstats.ProcessStatsUseCase;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {

    private final ProcessStatsUseCase useCase;

    private final Validator validator;

    public Mono<ServerResponse> handleStats(ServerRequest request) {
        return request.bodyToMono(UserStatsRequest.class)
                .flatMap(req -> {
                    // Validación manual
                    Errors errors = new BeanPropertyBindingResult(req,
                            UserStatsRequest.class.getName());
                    validator.validate(req, errors);

                    if (errors.hasErrors()) {
                        return Flux.fromIterable(errors.getFieldErrors())
                                .map(fieldError -> String.format("El campo '%s' %s",
                                        fieldError.getField(), fieldError.getDefaultMessage()))
                                .collectList()
                                .flatMap(messages ->
                                        ServerResponse.badRequest()
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .bodyValue(Map.of("errors", messages)));
                    }

                    // Si pasa validación, continúa
                    UserStats stats = UserStatsMapper.toDomain(req);
                    return useCase.processStats(stats)
                            .then(ServerResponse.ok()
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .bodyValue(Map.of("message",
                                            "Estadística procesada correctamente")));
                })
                .onErrorResume(IllegalArgumentException.class, ex ->
                        ServerResponse.badRequest()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(Map.of("error", ex.getMessage())));
    }
}
