package ru.mal.unialbumsbackend.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
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
    public void create(CreateAlbumRequest albumRequest, long userId, String fileName) {
        Album album = enrich(albumRequest,userId,fileName);
        albumRepository.save(album);
    }

    private Album enrich(CreateAlbumRequest albumRequest,long userId,String fileName) {
        Album album=new Album();
        album.setTitle(albumRequest.getTitle());
//        album.setCover("http://localhost:9000/images/"+fileName);
        album.setCover("http://79.174.95.140:9000/images/"+fileName);
        album.setAtmosphereRating(albumRequest.getAtmosphereRating());
        album.setBitsRating(albumRequest.getBitsRating());
        album.setTextRating(albumRequest.getTextRating());
        album.setTracksRating(albumRequest.getTracksRating());
        album.setArtist(albumRequest.getArtist());
        Optional<User> user=userService.findById(userId);
        if(user.isPresent()) {
            user.get().addAlbums(album);
            album.setUser(user.get());

        }

        return album;
    }
    public List<AlbumResponse> getAlbumsByUserId(Long userId) {
        return albumRepository.getAlbumByUserId(userId);

    }

}


