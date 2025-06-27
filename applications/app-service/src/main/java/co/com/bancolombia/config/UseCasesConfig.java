package co.com.bancolombia.config;

import co.com.bancolombia.model.events.gateways.EventsGateway;
import co.com.bancolombia.model.stats.gateways.HashValidator;
import co.com.bancolombia.model.stats.gateways.StatsRepository;
import co.com.bancolombia.usecase.processstats.ProcessStatsUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(basePackages = "co.com.bancolombia.usecase",
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "^.+UseCase$")
        },
        useDefaultFilters = false)
public class UseCasesConfig {

        @Bean
        public ProcessStatsUseCase processStatsUseCase(
                StatsRepository repository,
                EventsGateway publisher,
                HashValidator hashValidator
        ) {
                return new ProcessStatsUseCase(repository, publisher, hashValidator);
        }
}
