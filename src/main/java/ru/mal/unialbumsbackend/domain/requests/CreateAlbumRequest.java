package ru.mal.unialbumsbackend.domain.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAlbumRequest {

    private String title;

    private String cover;

    private int tracksRating;

    private int atmosphereRating;

    private int bitsRating;

    private int textRating;

    private String artist;

}