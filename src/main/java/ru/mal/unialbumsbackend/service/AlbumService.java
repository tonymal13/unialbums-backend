package ru.mal.unialbumsbackend.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mal.unialbumsbackend.domain.Album;
import ru.mal.unialbumsbackend.domain.requests.CreateAlbumRequest;
import ru.mal.unialbumsbackend.repositories.AlbumRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class AlbumService {

    private final AlbumRepository albumRepository;

    @Transactional
    public void create(CreateAlbumRequest albumRequest) {
        Album album = enrich(albumRequest);
        albumRepository.save(album);
    }

    private Album enrich(CreateAlbumRequest albumRequest) {
        Album album=new Album();
        album.setTitle(albumRequest.getTitle());
        album.setCover(albumRequest.getCover());
        album.setAtmosphereRating(albumRequest.getAtmosphereRating());
        album.setBitsRating(albumRequest.getBitsRating());
        album.setTextRating(albumRequest.getTextRating());
        album.setTracksRating(albumRequest.getTracksRating());
        return album;
    }

    public List<Album> getAllAlbums() {
        return albumRepository.findAll();
    }
}
