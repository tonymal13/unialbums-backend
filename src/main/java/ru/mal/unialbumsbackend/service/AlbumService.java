package ru.mal.unialbumsbackend.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mal.unialbumsbackend.domain.Album;
import ru.mal.unialbumsbackend.domain.User;
import ru.mal.unialbumsbackend.web.dto.album.CreateAlbumDto;
import ru.mal.unialbumsbackend.web.dto.album.AlbumDto;
import ru.mal.unialbumsbackend.repositories.AlbumRepository;

import java.util.*;

import static ru.mal.unialbumsbackend.util.config.WebConfig.host;

@Service
@AllArgsConstructor
public class AlbumService {
    private final AlbumRepository albumRepository;

    private final UserService userService;

    @Transactional
    public void create(CreateAlbumDto createAlbumDto, long userId, String fileName) {
        Album album = enrich(createAlbumDto,userId,fileName);
        albumRepository.save(album);
    }

    private Album enrich(CreateAlbumDto createAlbumDto, long userId, String fileName) {
        Album album=new Album();
        album.setTitle(createAlbumDto.getTitle());
        album.setCover(host+":9000/images/"+fileName);
        album.setAtmosphereRating(createAlbumDto.getAtmosphereRating());
        album.setBitsRating(createAlbumDto.getBitsRating());
        album.setTextRating(createAlbumDto.getTextRating());
        album.setTracksRating(createAlbumDto.getTracksRating());
        album.setArtist(createAlbumDto.getArtist());
        User user=userService.findById(userId);
        user.addAlbums(album);
        album.setUser(user);
        return album;
    }

    public List<AlbumDto> getAlbumsByUserId(Long userId) {
        return albumRepository.getAlbumByUserId(userId);

    }

    @Transactional
    public void save(Album album) {
        albumRepository.save(album);
    }
    public Album findById(long albumId) {
       return albumRepository.findById(albumId).orElseThrow(()->new RuntimeException("Альбом не найден"));
    }
}


