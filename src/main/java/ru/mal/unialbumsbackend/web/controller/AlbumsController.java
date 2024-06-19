package ru.mal.unialbumsbackend.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import ru.mal.unialbumsbackend.domain.Album;
import ru.mal.unialbumsbackend.web.dto.album.CreateAlbumDto;
import ru.mal.unialbumsbackend.web.dto.album.AlbumDto;
import ru.mal.unialbumsbackend.web.dto.BackendResponse;
import ru.mal.unialbumsbackend.service.AlbumService;
import ru.mal.unialbumsbackend.service.ImageService;

import java.util.*;

import static ru.mal.unialbumsbackend.web.dto.BackendResponse.initializeResponse;
import static ru.mal.unialbumsbackend.web.security.JwtUtils.decodeJWTGetHeader;

@RequestMapping("/api/v1/albums")
@RestController
@AllArgsConstructor
@Tag(name = "Albums Controller",description = "Albums API")
public class AlbumsController {

    private final AlbumService albumService;

    private final ImageService imageService;

    @PostMapping("/create")
    public ResponseEntity<BackendResponse> create(@RequestHeader(name = "Authorization") String jwt, @ModelAttribute("request") CreateAlbumDto createAlbumDto
            , @RequestParam("cover") MultipartFile cover
    )
    {
        JSONObject jsonObject = decodeJWTGetHeader(jwt);
        BackendResponse backendResponse=initializeResponse();
        backendResponse.setMessage("Альбом создан");
        long userId = ((Number)jsonObject.get("userId")).longValue();

        String filename= imageService.upload(cover);
        albumService.create(createAlbumDto,userId,filename);
        return ResponseEntity.ok(backendResponse);
    }

    @GetMapping("/getByUserId")
    public ResponseEntity<BackendResponse> getByUserId(@RequestHeader(name = "Authorization") String jwt){
        JSONObject jsonObject = decodeJWTGetHeader(jwt);

        long userId = ((Number) jsonObject.get("userId")).longValue();

         List<AlbumDto> albums= albumService.getAlbumsByUserId(userId);

         BackendResponse backendResponse=initializeResponse();
         backendResponse.setMessage("Альбомы пользователя:");

        for (AlbumDto album : albums) {
            addData(backendResponse,album);

        }
         return ResponseEntity.ok(backendResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BackendResponse> deleteAlbumById(@RequestHeader(name = "Authorization") String jwt,
                                                           @PathVariable ("id") long albumId){
        JSONObject jsonObject = decodeJWTGetHeader(jwt);

        long userId = ((Number) jsonObject.get("userId")).longValue();

        BackendResponse backendResponse = initializeResponse();

        AlbumDto albumDto = findAlbumByAlbumId(userId, albumId);

        if(albumDto !=null){

            albumService.deleteAlbumById(albumId);
            backendResponse.setMessage("Альбом удален");
        }
        else{
            backendResponse = new BackendResponse();
            backendResponse.setMessage("Вы не можете получить доступ к этому альбому");
        }
        return ResponseEntity.ok(backendResponse);
    }



    @PutMapping("/{id}")
    public ResponseEntity<?> editAlbumInfo(@PathVariable("id") long albumId, @RequestBody CreateAlbumDto req, @RequestHeader(name = "Authorization") String jwt){

        JSONObject jsonObject = decodeJWTGetHeader(jwt);

        long userId = ((Number) jsonObject.get("userId")).longValue();

        AlbumDto albumDto = findAlbumByAlbumId(userId, albumId);

        if(albumDto !=null){
            BackendResponse backendResponse = initializeResponse();
            Album album =albumService.findById(albumId);
            edit(album,req);
            albumService.save(album);
            backendResponse.setMessage("Данные успешно обновлены");
            return ResponseEntity.ok(backendResponse);
        }
        else{
            BackendResponse backendResponse = new BackendResponse();
            backendResponse.setMessage("Вы не можете получить доступ к этому альбому:");
            return new ResponseEntity<>(backendResponse,HttpStatus.BAD_REQUEST);
        }

    }

    private void edit(Album album, CreateAlbumDto createAlbumDto){
        album.setTitle(createAlbumDto.getTitle());
        album.setArtist(createAlbumDto.getArtist());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BackendResponse> getAlbumInfo(@PathVariable("id") long albumId, @RequestHeader(name = "Authorization") String jwt){
        JSONObject jsonObject = decodeJWTGetHeader(jwt);

        long userId = ((Number) jsonObject.get("userId")).longValue();
        BackendResponse backendResponse = initializeResponse();

        AlbumDto albumDto = findAlbumByAlbumId(userId, albumId);

        if(albumDto !=null){

            addData(backendResponse, albumDto);
            backendResponse.setMessage("Информация об альбоме:");
        }
        else{
            backendResponse = new BackendResponse();
            backendResponse.setMessage("Вы не можете получить доступ к этому альбому:");
        }
        return ResponseEntity.ok(backendResponse);
    }

    private AlbumDto findAlbumByAlbumId(long userId, long albumId) {

        List<AlbumDto> albums=albumService.getAlbumsByUserId(userId);

        AlbumDto albumDto =null;
        for(AlbumDto album:albums){
            if(album.getId()==albumId)
                albumDto =album;
        }

        return albumDto;
    }

    public void addData(BackendResponse universeResponse, AlbumDto albumDto){
        HashMap<String, String> map = new HashMap<>();
        universeResponse.addMap(map);
        universeResponse.addData(map, "title", albumDto.getTitle());
        universeResponse.addData(map, "cover", albumDto.getCover());
        universeResponse.addData(map, "tracksRating", Integer.toString(albumDto.getTracksRating()));
        universeResponse.addData(map, "atmosphereRating", Integer.toString(albumDto.getAtmosphereRating()));
        universeResponse.addData(map, "bitsRating", Integer.toString(albumDto.getBitsRating()));
        universeResponse.addData(map, "textRating", Integer.toString(albumDto.getTextRating()));
        universeResponse.addData(map, "artist", albumDto.getArtist());
        universeResponse.addData(map, "albumId", Long.toString(albumDto.getId()));
    }

}
