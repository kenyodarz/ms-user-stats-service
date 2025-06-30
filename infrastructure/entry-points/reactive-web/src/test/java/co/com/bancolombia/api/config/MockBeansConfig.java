package co.com.bancolombia.api.config;

import co.com.bancolombia.usecase.processstats.ProcessStatsUseCase;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.Validator;

@TestConfiguration
public class MockBeansConfig {

    @Bean
    public ProcessStatsUseCase processStatsUseCase() {
        return Mockito.mock(ProcessStatsUseCase.class);
    }

    @Bean
    public Validator validator() {
        return Mockito.mock(Validator.class);
    }
}

