package ru.mal.unialbumsbackend.web.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import ru.mal.unialbumsbackend.web.dto.album.AlbumRequest;
import ru.mal.unialbumsbackend.web.dto.album.AlbumResponse;
import ru.mal.unialbumsbackend.web.dto.BackendResponse;
import ru.mal.unialbumsbackend.service.AlbumService;
import ru.mal.unialbumsbackend.service.ImageService;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

@RequestMapping("/api/albums")
@RestController
public class AlbumsController {

    private final AlbumService albumService;

    private final ImageService imageService;


    @Value("{jwt.secret.access}")
    private String secret;

    public AlbumsController(AlbumService albumService, ImageService imageService) {

        this.albumService = albumService;
        this.imageService = imageService;
    }
    @PostMapping("/create")
    public ResponseEntity<BackendResponse> create(@RequestHeader(name = "Authorization") String jwt, @ModelAttribute("request") AlbumRequest request
            , @RequestParam("cover") MultipartFile cover
    )
    {
        JSONObject jsonObject = decodeJWTGetHeader(jwt);
        BackendResponse response=new BackendResponse();
        response.setMessage("Альбом создан");
        response.setData(new ArrayList<>());
        long userId = ((Number)jsonObject.get("userId")).longValue();

        String filename= imageService.upload(cover);
        albumService.create(request,userId,filename);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getByUserId")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BackendResponse> getAllAlbums(@RequestHeader(name = "Authorization") String jwt){
        JSONObject jsonObject = decodeJWTGetHeader(jwt);

        long userId = ((Number) jsonObject.get("userId")).longValue();

         List<AlbumResponse> albums= albumService.getAlbumsByUserId(userId);

         BackendResponse backendResponse =new BackendResponse();
         backendResponse.setData(new ArrayList<>());
         backendResponse.setMessage("Альбомы пользователя:");


        for (AlbumResponse album : albums) {
            HashMap<String, String> map = new HashMap<>();
            backendResponse.addMap(map);
            backendResponse.addData(map, "title", album.getTitle());
            backendResponse.addData(map, "cover", album.getCover());
            backendResponse.addData(map, "tracksRating", Integer.toString(album.getTracksRating()));
            backendResponse.addData(map, "atmosphereRating", Integer.toString(album.getAtmosphereRating()));
            backendResponse.addData(map, "bitsRating", Integer.toString(album.getBitsRating()));
            backendResponse.addData(map, "textRating", Integer.toString(album.getTextRating()));
            backendResponse.addData(map, "artist", album.getArtist());
            backendResponse.addData(map, "albumId", Long.toString(album.getId()));
        }
         return ResponseEntity.ok(backendResponse);
    }

    public static JSONObject decodeJWTGetHeader(String jwt){
        jwt= jwt.replace("Bearer ", "");
        String[] chunks=jwt.split("\\.");

        Base64.Decoder decoder=Base64.getUrlDecoder();
        String payload=new String(decoder.decode(chunks[1]));
        return new JSONObject(payload);
    }

}
