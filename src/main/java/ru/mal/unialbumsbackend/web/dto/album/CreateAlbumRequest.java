package ru.mal.unialbumsbackend.web.dto.album;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class CreateAlbumRequest {

    private String title;

    private int tracksRating;

    private int atmosphereRating;

    private int bitsRating;

    private int textRating;

    private String artist;

}