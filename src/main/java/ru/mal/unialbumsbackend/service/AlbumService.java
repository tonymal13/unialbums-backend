package ru.mal.unialbumsbackend.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mal.unialbumsbackend.domain.Album;
import ru.mal.unialbumsbackend.domain.User;
import ru.mal.unialbumsbackend.domain.requests.CreateAlbumRequest;
import ru.mal.unialbumsbackend.domain.response.AlbumResponse;
import ru.mal.unialbumsbackend.repositories.AlbumRepository;

import java.util.*;


@Service
@AllArgsConstructor
public class AlbumService {

    private final AlbumRepository albumRepository;

    private final UserService userService;

    @Transactional
    public void create(CreateAlbumRequest albumRequest) {
        System.out.println(albumRequest.toString());
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
        album.setArtist(albumRequest.getArtist());
        Optional<User> user=userService.findById(albumRequest.getUserId());
        if(user.isPresent()) {
            user.get().addAlbums(album);
            System.out.println("albums:"+user.get().getAlbums().get(0).getTitle());
            album.setUser(user.get());

        }

        return album;
    }
    public List<AlbumResponse> getAlbumsByUserId(Long userId) {
        return albumRepository.getAlbumByUserId();

    }
}


