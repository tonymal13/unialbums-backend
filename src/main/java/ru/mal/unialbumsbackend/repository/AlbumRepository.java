package ru.mal.unialbumsbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.mal.unialbumsbackend.domain.Album;
import ru.mal.unialbumsbackend.web.dto.album.AlbumResponse;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album,Long> {
    @Query("SELECT new ru.mal.unialbumsbackend.web.dto.album.AlbumResponse(albums.id,albums.title, albums.cover,albums.tracksRating,albums.atmosphereRating,albums.bitsRating,albums.textRating,albums.artist) FROM User user JOIN user.albums albums where user.id=:userId")
    List<AlbumResponse> getAlbumByUserId(@Param("userId")Long userId);

}
