package ru.mal.unialbumsbackend.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mal.unialbumsbackend.domain.Album;
import ru.mal.unialbumsbackend.domain.User;

import ru.mal.unialbumsbackend.web.dto.album.CreateAlbumDto;
import ru.mal.unialbumsbackend.web.dto.album.AlbumDto;
import ru.mal.unialbumsbackend.repositories.AlbumRepository;

import java.util.*;


@Service
@AllArgsConstructor
public class AlbumService {

//    @Value("${host}")
//    private String host;
    private final AlbumRepository albumRepository;

    private final UserService userService;

    @Transactional
    public void create(CreateAlbumDto albumRequest, long userId, String fileName) {
        Album album = enrich(albumRequest,userId,fileName);
        albumRepository.save(album);
    }

    private Album enrich(CreateAlbumDto albumRequest, long userId, String fileName) {
        Album album=new Album();
        album.setTitle(albumRequest.getTitle());
//        album.setCover("http://"+host+":9000/images/"+fileName);
            album.setCover("http://89.111.172.174:9000/images/"+fileName);

//            album.setCover("http://89.111.172.174:9000/images/"+fileName);
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

    public List<AlbumDto> getAlbumsByUserId(Long userId) {
        return albumRepository.getAlbumByUserId(userId);

    }

    @Transactional
    public void save(Album album) {
//        Album album= edit(albumRe,userId);
//        System.out.println(album.toString());
        albumRepository.save(album);
    }

    public AlbumDto getAlbumById(long albumId) {
        return albumRepository.getAlbumById(albumId);
    }

    @Transactional
    public Optional<Album> findById(long albumId) {
       return albumRepository.findById(albumId);
    }
}


