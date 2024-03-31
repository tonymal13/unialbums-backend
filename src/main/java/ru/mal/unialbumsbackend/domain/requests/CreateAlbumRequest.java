package ru.mal.unialbumsbackend.domain.requests;

import lombok.Getter;
import org.hibernate.annotations.SecondaryRow;

@Getter
@SecondaryRow
public class CreateAlbumRequest {

    private String title;

    private String cover;

    private double tracksRating;

    private double atmosphereRating;

    private double bitsRating;

    private double textRating;

    private String artist;

}