package ru.mal.unialbumsbackend.spotify_client_module.dto;

import lombok.Data;

import java.util.List;

@Data
public class AlbumDTO {

    private String name;

    private String id;

    private List<String> artists;

    private String image;

}

