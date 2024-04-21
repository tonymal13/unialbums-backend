package ru.mal.unialbumsbackend.web.mappers;

import org.mapstruct.Mapper;
import ru.mal.unialbumsbackend.domain.Album;
import ru.mal.unialbumsbackend.web.dto.album.AlbumRequest;

@Mapper(componentModel = "spring")
public class AlbumMapper implements Mappable<Album, AlbumRequest> {

    @Override
    public Album toEntity(AlbumRequest albumRequest) {
        Album album=new Album();
        album.setTitle(albumRequest.getTitle());
//        album.setCover("http://localhost:9000/images/"+fileName);
        album.setAtmosphereRating(albumRequest.getAtmosphereRating());
        album.setBitsRating(albumRequest.getBitsRating());
        album.setTextRating(albumRequest.getTextRating());
        album.setTracksRating(albumRequest.getTracksRating());
        album.setArtist(albumRequest.getArtist());
        return album;
    }

    @Override
    public AlbumRequest toDto(Album entity) {
        return null;
    }
}
