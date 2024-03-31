package ru.mal.unialbumsbackend.domain.response;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.mal.unialbumsbackend.domain.User;

@Getter
@Setter
@AllArgsConstructor
public class AlbumResponse {

    private Long id;

    private String title;

    private String cover;

    private double tracksRating;

    private double atmosphereRating;

    private double bitsRating;

    private double textRating;

    private String artist;

}
