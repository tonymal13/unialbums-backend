package ru.mal.unialbumsbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.mal.unialbumsbackend.domain.Album;
import ru.mal.unialbumsbackend.domain.response.AlbumResponse;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album,Long> {
    @Query("SELECT new ru.mal.unialbumsbackend.domain.response.AlbumResponse(albums.id,albums.title, albums.cover,albums.tracksRating,albums.atmosphereRating,albums.bitsRating,albums.textRating,albums.artist) FROM User user JOIN user.albums albums")
    List<AlbumResponse> getAlbumByUserId();

}