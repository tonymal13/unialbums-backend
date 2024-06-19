package ru.mal.unialbumsbackend.web.dto.album;

import lombok.Data;

@Data
public class CreateAlbumDto {

    private String title;

    private int tracksRating;

    private int atmosphereRating;

    private int bitsRating;

    private int textRating;

    private String artist;

}