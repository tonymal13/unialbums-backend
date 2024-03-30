package ru.mal.unialbumsbackend.domain.requests;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.hibernate.annotations.SecondaryRow;
import ru.mal.unialbumsbackend.domain.User;

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

    private Long userId;

}
