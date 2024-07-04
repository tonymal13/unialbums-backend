package ru.mal.unialbumsbackend.spotify_client_module.controller;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.mal.unialbumsbackend.spotify_client_module.client.SpotifyAPIClient;
import ru.mal.unialbumsbackend.spotify_client_module.dto.SpotifyAlbumDTO;
import ru.mal.unialbumsbackend.spotify_client_module.entity.Artist;


import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/v1/spotify")
public class AlbumRestController {

    private final SpotifyAPIClient spotifyAPIClient;

    public AlbumRestController(SpotifyAPIClient spotifyAPIClient) {
        this.spotifyAPIClient = spotifyAPIClient;
    }

    @GetMapping("/albums")
    public Mono<List<SpotifyAlbumDTO>> getAlbums(@RequestParam("q") String query, @RequestHeader(name = "Authorization") String jwt){

        return spotifyAPIClient. getAlbumsByTitle(query)
                .map(albumResponse -> albumResponse.getAlbums().getItems().stream()
                        .map(albumItem -> {
                            SpotifyAlbumDTO dto = new SpotifyAlbumDTO();
                            dto.setId(albumItem.getId());
                            dto.setName(albumItem.getName());
                            // Извлекаем только имена исполнителей
                            List<String> artistNames = albumItem.getArtists().stream()
                                    .map(Artist::getName) // Получаем имя исполнителя
                                    .collect(Collectors.toList());
                            dto.setArtists(artistNames);
                            dto.setImage(albumItem.getImages().get(2).getUrl());
                            return dto;
                        })
                        .collect(Collectors.toList()));
    }
}

