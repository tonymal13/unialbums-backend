package ru.mal.unialbumsbackend.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name="albums")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Album {

    @Id
    @Column(name = "id",nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;

    @Column(name = "title",nullable = false)
    @Size(max = 100)
    private String title;

    @Column(name = "cover",nullable = false)
    @Size(max = 100)
    private String cover;

    @Column(name = "tracks_rating",nullable = false)
    private int tracksRating;

    @Column(name = "atmosphere_rating",nullable = false)
    private int atmosphereRating;

    @Column(name = "bits_rating",nullable = false)
    private int bitsRating;

    @Column(name = "text_rating",nullable = false)
    private int textRating;

    @Column(name="artist",nullable = false)
    private String artist;

}
