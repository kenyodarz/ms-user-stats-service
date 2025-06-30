package co.com.bancolombia.api.handler;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import co.com.bancolombia.api.request.UserStatsRequest;
import co.com.bancolombia.model.stats.UserStats;
import co.com.bancolombia.usecase.processstats.ProcessStatsUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class StatsHandlerTest {

    private StatsHandler handler;
    private ProcessStatsUseCase useCase;
    private Validator validator;

    @BeforeEach
    void setUp() {
        useCase = mock(ProcessStatsUseCase.class);
        validator = mock(Validator.class);
        handler = new StatsHandler(useCase, validator);
    }

    @Test
    void getStatus_shouldReturnOk() {
        Mono<ServerResponse> responseMono = handler.getStatus();

        StepVerifier.create(responseMono)
                .expectNextMatches(response ->
                        response.statusCode().is2xxSuccessful() &&
                                MediaType.APPLICATION_JSON.equals(
                                        response.headers().getContentType()))
                .verifyComplete();
    }


    @Test
    void handleStats_validRequest_shouldReturnOk() {
        UserStatsRequest request = new UserStatsRequest();
        request.setTotalContactoClientes(100);
        request.setMotivoReclamo(10);
        request.setMotivoGarantia(5);
        request.setMotivoDuda(20);
        request.setMotivoCompra(30);
        request.setMotivoFelicitaciones(3);
        request.setMotivoCambio(2);
        request.setHash("123456");

        when(useCase.processStats(any(UserStats.class))).thenReturn(Mono.empty());
        doAnswer(invocation -> null).when(validator).validate(any(), any());

        ServerRequest serverRequest = MockServerRequest.builder()
                .method(HttpMethod.POST)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(request));

        Mono<ServerResponse> responseMono = handler.handleStats(serverRequest);

        StepVerifier.create(responseMono)
                .expectNextMatches(response ->
                        response.statusCode().is2xxSuccessful())
                .verifyComplete();
    }


    @Test
    void handleStats_invalidRequest_shouldReturnBadRequest() {
        UserStatsRequest invalidRequest = new UserStatsRequest(); // campos vacíos

        doAnswer(invocation -> {
            org.springframework.validation.Errors errors = invocation.getArgument(1);
            errors.rejectValue("totalContactoClientes", "field.required", "es obligatorio");
            return null;
        }).when(validator).validate(any(), any());

        ServerRequest serverRequest = MockServerRequest.builder()
                .method(HttpMethod.POST)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(invalidRequest));

        Mono<ServerResponse> responseMono = handler.handleStats(serverRequest);

        StepVerifier.create(responseMono)
                .expectNextMatches(response ->
                        response.statusCode().is4xxClientError())
                .verifyComplete();
    }

    @Test
    void handleStats_shouldHandleIllegalArgumentException() {
        UserStatsRequest request = new UserStatsRequest();
        request.setTotalContactoClientes(100);
        request.setMotivoReclamo(10);
        request.setMotivoGarantia(5);
        request.setMotivoDuda(20);
        request.setMotivoCompra(30);
        request.setMotivoFelicitaciones(3);
        request.setMotivoCambio(2);
        request.setHash("123456");

        when(useCase.processStats(any(UserStats.class)))
                .thenThrow(new IllegalArgumentException("Hash inválido"));
        doAnswer(invocation -> null).when(validator).validate(any(), any());

        ServerRequest serverRequest = MockServerRequest.builder()
                .method(HttpMethod.POST)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(request));

        Mono<ServerResponse> responseMono = handler.handleStats(serverRequest);

        StepVerifier.create(responseMono)
                .expectNextMatches(response ->
                        response.statusCode().is4xxClientError())
                .verifyComplete();
    }
}
