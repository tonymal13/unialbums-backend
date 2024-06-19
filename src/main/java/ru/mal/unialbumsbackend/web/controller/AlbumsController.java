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
        long userId = ((Number)jsonObject.get("userId")).longValue();

        BackendResponse backendResponse=initializeResponse();
        backendResponse.setMessage("Альбом создан");

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
            addDataToResponse(backendResponse,album);

        }
         return ResponseEntity.ok(backendResponse);
    }


    @DeleteMapping("/{albumId}")
    public ResponseEntity<BackendResponse> deleteAlbumById(@RequestHeader(name = "Authorization") String jwt,
                                                           @PathVariable ("albumId") long albumId){
        JSONObject jsonObject = decodeJWTGetHeader(jwt);
        long userId = ((Number) jsonObject.get("userId")).longValue();

        BackendResponse backendResponse = initializeResponse();

        AlbumDto albumDto = findAlbumByAlbumId(userId, albumId);

        if(albumDto !=null){
            albumService.deleteAlbumById(albumId);
            backendResponse.setMessage("Альбом удален");
        }
        else{
            backendResponse = initializeResponse();
            backendResponse.setMessage("Вы не можете получить доступ к этому альбому");
        }
        return ResponseEntity.ok(backendResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editAlbumInfo(@PathVariable("id") long albumId, @RequestBody CreateAlbumDto createAlbumDto, @RequestHeader(name = "Authorization") String jwt){

        JSONObject jsonObject = decodeJWTGetHeader(jwt);
        long userId = ((Number) jsonObject.get("userId")).longValue();

        AlbumDto albumDto = findAlbumByAlbumId(userId, albumId);

        if(albumDto !=null){
            BackendResponse backendResponse = initializeResponse();
            Album album =albumService.findById(albumId);
            editAlbum(album,createAlbumDto);
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

    private void editAlbum(Album album, CreateAlbumDto createAlbumDto){
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

            addDataToResponse(backendResponse, albumDto);
            backendResponse.setMessage("Информация об альбоме:");
        }
        else{
            backendResponse = new BackendResponse();
            backendResponse.setMessage("Вы не можете получить доступ к этому альбому:");
        }
        return ResponseEntity.ok(backendResponse);
    }

    private AlbumDto findAlbumByAlbumId(long userId, long albumId) {
        return albumService.getAlbumsByUserId(userId).stream()
                .filter(album -> album.getId() == albumId)
                .findFirst()
                .orElse(null);
    }

    public void addDataToResponse(BackendResponse backendResponse, AlbumDto albumDto){
        HashMap<String, String> map = new HashMap<>();
        backendResponse.addMap(map);
        backendResponse.addData(map, "title", albumDto.getTitle());
        backendResponse.addData(map, "cover", albumDto.getCover());
        backendResponse.addData(map, "tracksRating", Integer.toString(albumDto.getTracksRating()));
        backendResponse.addData(map, "atmosphereRating", Integer.toString(albumDto.getAtmosphereRating()));
        backendResponse.addData(map, "bitsRating", Integer.toString(albumDto.getBitsRating()));
        backendResponse.addData(map, "textRating", Integer.toString(albumDto.getTextRating()));
        backendResponse.addData(map, "artist", albumDto.getArtist());
        backendResponse.addData(map, "albumId", Long.toString(albumDto.getId()));
    }

}
