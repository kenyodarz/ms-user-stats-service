package co.com.bancolombia.api.config;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    private static final String LOCALHOST_ORIGIN = "http://localhost:4200";


    private static final List<String> ALLOWED_METHODS = Arrays.asList(GET.name(), POST.name(),
            PUT.name(), DELETE.name(), OPTIONS.name());

    @Profile("local")
    @Bean
    CorsWebFilter corsWebFilterLocal() {
        return createCorsFilter(Collections.singletonList(LOCALHOST_ORIGIN));
    }

    @Profile({"dev", "qa"})
    @Bean
    CorsWebFilter corsWebFilterOthers(@Value("${cors.allowed-origin}") String origin) {
        return createCorsFilter(getOriginsList(origin, true));
    }

    @Profile("pdn")
    @Bean
    CorsWebFilter corsWebFilterPdn(@Value("${cors.allowed-origin}") String origin) {
        return createCorsFilter(getOriginsList(origin, false));
    }

    private CorsWebFilter createCorsFilter(List<String> allowedOrigins) {
        var config = new CorsConfiguration();
        config.setAllowCredentials(Boolean.TRUE);
        config.setAllowedOrigins(allowedOrigins);
        config.setAllowedMethods(ALLOWED_METHODS);
        config.setAllowedHeaders(Collections.singletonList(CorsConfiguration.ALL));

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsWebFilter(source);
    }

    private List<String> getOriginsList(String origin, boolean includeLocalhost) {
        List<String> originList = new ArrayList<>(Arrays.asList(origin.split(",")));
        if (includeLocalhost) {
            originList.add(LOCALHOST_ORIGIN);
        }
        return originList;
    }
}
