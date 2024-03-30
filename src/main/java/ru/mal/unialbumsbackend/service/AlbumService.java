package ru.mal.unialbumsbackend.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mal.unialbumsbackend.domain.Album;
import ru.mal.unialbumsbackend.domain.User;
import ru.mal.unialbumsbackend.domain.requests.CreateAlbumRequest;
import ru.mal.unialbumsbackend.repositories.AlbumRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        if(album==null){
            System.out.println("nuuuuuuuuuuuuuuuuuuuuuuuuuuuluuuuuuuuuuuuu");
        }

        album.setTitle(albumRequest.getTitle());
        album.setCover(albumRequest.getCover());
        album.setAtmosphereRating(albumRequest.getAtmosphereRating());
        album.setBitsRating(albumRequest.getBitsRating());
        album.setTextRating(albumRequest.getTextRating());
        album.setTracksRating(albumRequest.getTracksRating());
        album.setArtist(albumRequest.getArtist());
        Optional<User> user=userService.findById(albumRequest.getUserId());
        if(user.isPresent()) {
            System.out.println(user.toString());
            user.get().setAlbums(List.of(album));
            album.setUser(user.get());

            System.out.println(album.toString());

        }

        return album;
    }

    public List<Album> getAllAlbums() {
        return albumRepository.findAll();
    }

    public List<Album> getAlbumsByUserId(Long userId) {
        return albumRepository.findAllByUserId(userId);
    }
}
