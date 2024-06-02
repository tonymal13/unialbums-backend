package ru.mal.unialbumsbackend.util.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;

import static ru.mal.unialbumsbackend.util.config.WebConfig.host;

@Configuration
@EnableWebMvc
@CrossOrigin(origins ="http://localhost:3000,http://localhost:6006,"
        +host+","
        +host+":3000,"
        +host+":9000,"
        +host+":9002,"
        +host+":6006"
        ,allowCredentials = "true")
public class WebConfig {

    private static final Long MAX_AGE = 3600L;

//    public static final String host="http://45.89.188.224";

    public static final String host="http://localhost";

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin(host);
        config.addAllowedOrigin(host+":3000");
        config.addAllowedOrigin(host+":9000");
        config.addAllowedOrigin(host+":9002");
        config.setAllowedHeaders(Arrays.asList(
                HttpHeaders.AUTHORIZATION,
                HttpHeaders.CONTENT_TYPE,
                HttpHeaders.ACCEPT));
        config.setAllowedMethods(Arrays.asList(
                HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.PUT.name(),
                HttpMethod.DELETE.name()));
        config.setMaxAge(MAX_AGE);
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
