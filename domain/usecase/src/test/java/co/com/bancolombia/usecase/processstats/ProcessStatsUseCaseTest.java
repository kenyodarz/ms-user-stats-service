package co.com.bancolombia.usecase.processstats;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import co.com.bancolombia.model.events.gateways.EventsGateway;
import co.com.bancolombia.model.stats.UserStats;
import co.com.bancolombia.model.stats.gateways.HashValidator;
import co.com.bancolombia.model.stats.gateways.StatsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class ProcessStatsUseCaseTest {

    private StatsRepository repository;
    private EventsGateway publisher;
    private HashValidator validator;
    private ProcessStatsUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(StatsRepository.class);
        publisher = mock(EventsGateway.class);
        validator = mock(HashValidator.class);
        useCase = new ProcessStatsUseCase(repository, publisher, validator);
    }

    @Test
    void shouldProcessStatsSuccessfully() {
        UserStats input = new UserStats(250, 25, 10, 100, 100, 7, 8, "validhash",
                "2024-01-01T00:00:00Z");
        when(validator.isValid(input)).thenReturn(true);
        when(repository.save(any())).thenReturn(Mono.just(input));
        when(publisher.emit(any())).thenReturn(Mono.empty());

        StepVerifier.create(useCase.processStats(input))
                .expectNextMatches(result -> result == input)
                .verifyComplete();

        verify(repository).save(input);
        verify(publisher).emit(any());
    }

    @Test
    void shouldFailWhenHashIsInvalid() {
        UserStats input = new UserStats(250, 25, 10, 100, 100, 7, 8, "invalidhash",
                "2024-01-01T00:00:00Z");

        when(validator.isValid(input)).thenReturn(false);

        StepVerifier.create(useCase.processStats(input))
                .expectErrorMatches(e -> e instanceof IllegalArgumentException &&
                        e.getMessage().equals("Hash inválido"))
                .verify();

        verify(repository, never()).save(any());
        verify(publisher, never()).emit(any());
    }

    @Test
    void shouldPropagateErrorWhenRepositoryFails() {
        UserStats input = new UserStats(250, 25, 10, 100, 100, 7, 8, "validhash",
                "2024-01-01T00:00:00Z");

        when(validator.isValid(input)).thenReturn(true);
        when(repository.save(any())).thenReturn(Mono.error(new RuntimeException("DB failure")));

        StepVerifier.create(useCase.processStats(input))
                .expectErrorMatches(e -> e instanceof RuntimeException &&
                        e.getMessage().equals("DB failure"))
                .verify();

        verify(repository).save(input);
        verify(publisher, never()).emit(any());
    }
}
