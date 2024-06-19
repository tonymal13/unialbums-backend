package ru.mal.unialbumsbackend.web.dto.album;

import lombok.Data;

@Data
public class AlbumDto {

    private Long id;

    private String title;

    private String cover;

    private int tracksRating;

    private int atmosphereRating;

    private int bitsRating;

    private int textRating;

    private String artist;


}
