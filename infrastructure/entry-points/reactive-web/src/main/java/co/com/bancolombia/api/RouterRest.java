package co.com.bancolombia.api;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import co.com.bancolombia.api.handler.StatsHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@RequiredArgsConstructor
public class RouterRest {

    private final StatsHandler handler;

    @Bean
    public RouterFunction<ServerResponse> statsRoutes() {
        return route(
                POST(ApiPaths.STATS_VALIDATE), handler::handleStats)
                .andRoute(GET(ApiPaths.STATUS), request -> handler.getStatus());
    }
}
