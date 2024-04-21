package ru.mal.unialbumsbackend.web.dto.album;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AlbumResponse {

    private Long id;

    private String title;

    private String cover;

    private int tracksRating;

    private int atmosphereRating;

    private int bitsRating;

    private int textRating;

    private String artist;


}
