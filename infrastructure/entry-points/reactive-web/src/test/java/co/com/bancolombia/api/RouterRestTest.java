package co.com.bancolombia.api;

import co.com.bancolombia.api.config.MockBeansConfig;
import co.com.bancolombia.api.handler.StatsHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

@ContextConfiguration(classes = {RouterRest.class, StatsHandler.class, MockBeansConfig.class})
@WebFluxTest
class RouterRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void shouldRespondOkToStatus() {
        webTestClient.get()
                .uri("/v1/status")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.status").isEqualTo("ok");
    }
}
