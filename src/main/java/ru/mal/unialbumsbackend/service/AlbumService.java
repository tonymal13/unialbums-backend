package ru.mal.unialbumsbackend.service;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mal.unialbumsbackend.domain.Album;
import ru.mal.unialbumsbackend.domain.User;
import ru.mal.unialbumsbackend.web.dto.album.CreateAlbumDto;
import ru.mal.unialbumsbackend.web.dto.album.AlbumDto;
import ru.mal.unialbumsbackend.repositories.AlbumRepository;

import java.util.*;

import static ru.mal.unialbumsbackend.service.config.WebConfig.host;

@Service
@AllArgsConstructor
public class AlbumService {
    private final AlbumRepository albumRepository;

    private final UserService userService;

    private ModelMapper modelMapper;

    @Transactional
    public void create(CreateAlbumDto createAlbumDto, long userId, String fileName) {
        Album album = enrich(createAlbumDto,userId,fileName);
        albumRepository.save(album);
    }

    public Album enrich(CreateAlbumDto createAlbumDto, long userId, String fileName) {
        Album album = modelMapper.map(createAlbumDto, Album.class);
        album.setCover(host + ":9000/images/" + fileName);

        User user = userService.findById(userId);
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

    @Transactional
    public void deleteAlbumById(long albumId) {
        albumRepository.deleteById(albumId);
    }
}


