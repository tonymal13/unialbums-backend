package ru.mal.unialbumsbackend.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mal.unialbumsbackend.domain.Album;
import ru.mal.unialbumsbackend.domain.User;

import ru.mal.unialbumsbackend.web.dto.album.AlbumRequest;
import ru.mal.unialbumsbackend.web.dto.album.AlbumResponse;
import ru.mal.unialbumsbackend.web.mappers.AlbumMapper;
import ru.mal.unialbumsbackend.repository.AlbumRepository;

import java.util.*;


@Service
@AllArgsConstructor
public class AlbumService {

    private final AlbumRepository albumRepository;

    private final UserService userService;

    private final AlbumMapper requestMapper;

    @Transactional
    public void create(AlbumRequest albumRequest, long userId, String fileName) {
        Album album = enrich(albumRequest,userId,fileName);
        albumRepository.save(album);
    }

    private Album enrich(AlbumRequest albumRequest, long userId, String fileName) {
        Album album=requestMapper.toEntity(albumRequest);
        album.setCover("http://localhost:9000/images/"+fileName);
//        album.setCover("http://79.174.95.140:9000/images/"+fileName);

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


