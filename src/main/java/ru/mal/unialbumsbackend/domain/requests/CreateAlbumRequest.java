package ru.mal.unialbumsbackend.domain.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAlbumRequest {

    private String title;

    private String cover;

    private double tracksRating;

    private double atmosphereRating;

    private double bitsRating;

    private double textRating;

    private String artist;

}