package co.com.bancolombia.events;

import co.com.bancolombia.model.events.gateways.EventsGateway;
import java.util.UUID;
import java.util.logging.Level;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.reactivecommons.api.domain.DomainEvent;
import org.reactivecommons.api.domain.DomainEventBus;
import org.reactivecommons.async.impl.config.annotations.EnableDomainEventBus;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Log
@RequiredArgsConstructor
@EnableDomainEventBus
@Component
public class ReactiveEventsGateway implements EventsGateway {

    public static final String EVENT_NAME = "event.stats.validated";
    private final DomainEventBus domainEventBus;

    @Override
    public Mono<Void> emit(Object event) {
        log.log(Level.INFO, "Sending domain event: {0}: {1}",
                new String[]{EVENT_NAME, event.toString()});
        return Mono.from(domainEventBus.emit(
                new DomainEvent<>(EVENT_NAME, UUID.randomUUID().toString(), event)));
    }
}
