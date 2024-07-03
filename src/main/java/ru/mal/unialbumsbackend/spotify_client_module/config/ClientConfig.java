package ru.mal.unialbumsbackend.spotify_client_module.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class ClientConfig {

    @Value("${client.id}") String clientId;

    @Value("${client.secret}") String clientSecret;

}
