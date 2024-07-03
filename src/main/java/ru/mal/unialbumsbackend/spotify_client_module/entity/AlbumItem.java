package ru.mal.unialbumsbackend.spotify_client_module.entity;

import lombok.Data;

import java.util.List;

@Data
public class AlbumItem {
    private String album_type;
    private List<Artist> artists;
    private List<String> available_markets;
    private ExternalUrl external_urls;
    private String href;
    private String id;
    private List<Image> images;
    private String name;
    private String release_date;
    private String release_date_precision;
    private int total_tracks;
    private String type;
    private String uri;
}
