package co.com.bancolombia.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomRabbitMQConfig  {

    public static final String QUEUE_NAME = "event.stats.validated";
    public static final String EXCHANGE_NAME = "reactivecommons.domain-events";

    @Bean
    public Queue statsValidatedQueue() {
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public TopicExchange reactiveCommonsExchange() {
        return new TopicExchange(EXCHANGE_NAME, true, false);
    }

    @Bean
    public Binding statsValidatedBinding(Queue statsValidatedQueue, TopicExchange reactiveCommonsExchange) {
        return BindingBuilder.bind(statsValidatedQueue)
                .to(reactiveCommonsExchange)
                .with(QUEUE_NAME);
    }
}
