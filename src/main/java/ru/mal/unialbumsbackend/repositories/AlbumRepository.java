package ru.mal.unialbumsbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.mal.unialbumsbackend.domain.Album;
import ru.mal.unialbumsbackend.web.dto.album.AlbumDto;
import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album,Long> {
    @Query("SELECT new ru.mal.unialbumsbackend.web.dto.album .AlbumDto(albums.id,albums.title, albums.cover,albums.tracksRating,albums.atmosphereRating,albums.bitsRating,albums.textRating,albums.artist) FROM User user JOIN user.albums albums where user.id=:userId")
    List<AlbumDto> getAlbumByUserId(@Param("userId")Long userId);

//    @Query("SELECT new ru.mal.unialbumsbackend.web.dto.album .AlbumDto(id,title,cover,tracksRating,atmosphereRating,bitsRating,textRating,artist) FROM Album where id =:albumId")
//    AlbumDto getAlbumById(@Param("albumId") Long albumId);
}
