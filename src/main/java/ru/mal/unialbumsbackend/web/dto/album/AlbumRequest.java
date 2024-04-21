package ru.mal.unialbumsbackend.web.dto.album;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlbumRequest {

    private String title;

    private int tracksRating;

    private int atmosphereRating;

    private int bitsRating;

    private int textRating;

    private String artist;

}