package ru.mal.unialbumsbackend.spotify_client_module.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true) // Игнорировать неизвестные поля
public class Artist {
    private String href;
    private String id;
    private String name;
    private String type;
    private String uri;
}