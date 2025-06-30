package co.com.bancolombia.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import co.com.bancolombia.model.events.gateways.EventsGateway;
import co.com.bancolombia.model.stats.gateways.HashValidator;
import co.com.bancolombia.model.stats.gateways.StatsRepository;
import co.com.bancolombia.usecase.processstats.ProcessStatsUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

class UseCasesConfigTest {

    @Test
    void shouldLoadProcessStatsUseCaseBean() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                TestConfig.class)) {
            ProcessStatsUseCase useCase = context.getBean(ProcessStatsUseCase.class);
            assertNotNull(useCase, "ProcessStatsUseCase bean should be loaded");
        }
    }

    @Configuration
    @Import(UseCasesConfig.class)
    static class TestConfig {

        @Bean
        public StatsRepository statsRepository() {
            return mock(StatsRepository.class);
        }

        @Bean
        public EventsGateway eventsGateway() {
            return mock(EventsGateway.class);
        }

        @Bean
        public HashValidator hashValidator() {
            return mock(HashValidator.class);
        }
    }
}
