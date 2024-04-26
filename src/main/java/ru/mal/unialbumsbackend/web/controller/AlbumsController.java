package ru.mal.unialbumsbackend.web.controller;

import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import ru.mal.unialbumsbackend.web.dto.album.CreateAlbumRequest;
import ru.mal.unialbumsbackend.web.dto.album.AlbumResponse;
import ru.mal.unialbumsbackend.web.dto.UniverseResponse;
import ru.mal.unialbumsbackend.service.AlbumService;
import ru.mal.unialbumsbackend.service.ImageService;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

@RequestMapping("/api/albums")
@RestController
@AllArgsConstructor
public class AlbumsController {

    private final AlbumService albumService;

    private final ImageService imageService;

    @PostMapping("/create")
    public ResponseEntity<UniverseResponse> create(@RequestHeader(name = "Authorization") String jwt, @ModelAttribute("request") CreateAlbumRequest request
            , @RequestParam("cover") MultipartFile cover
    )
    {
        JSONObject jsonObject = decodeJWTGetHeader(jwt);
        UniverseResponse response=new UniverseResponse();
        response.setMessage("Альбом создан");
        response.setData(new ArrayList<>());
        long userId = ((Number)jsonObject.get("userId")).longValue();

        String filename= imageService.upload(cover);
        albumService.create(request,userId,filename);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getByUserId")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UniverseResponse> getAllAlbums(@RequestHeader(name = "Authorization") String jwt){
        JSONObject jsonObject = decodeJWTGetHeader(jwt);

        long userId = ((Number) jsonObject.get("userId")).longValue();

         List<AlbumResponse> albums= albumService.getAlbumsByUserId(userId);

         UniverseResponse universeResponse=new UniverseResponse();
         universeResponse.setData(new ArrayList<>());
         universeResponse.setMessage("Альбомы пользователя:");


        for (AlbumResponse album : albums) {
            HashMap<String, String> map = new HashMap<>();
            universeResponse.addMap(map);
            universeResponse.addData(map, "title", album.getTitle());
            universeResponse.addData(map, "cover", album.getCover());
            universeResponse.addData(map, "tracksRating", Integer.toString(album.getTracksRating()));
            universeResponse.addData(map, "atmosphereRating", Integer.toString(album.getAtmosphereRating()));
            universeResponse.addData(map, "bitsRating", Integer.toString(album.getBitsRating()));
            universeResponse.addData(map, "textRating", Integer.toString(album.getTextRating()));
            universeResponse.addData(map, "artist", album.getArtist());
            universeResponse.addData(map, "albumId", Long.toString(album.getId()));
        }
         return ResponseEntity.ok(universeResponse);
    }

    public static JSONObject decodeJWTGetHeader(String jwt){
        jwt= jwt.replace("Bearer ", "");
        String[] chunks=jwt.split("\\.");

        Base64.Decoder decoder=Base64.getUrlDecoder();
        String payload=new String(decoder.decode(chunks[1]));
        return new JSONObject(payload);
    }

}
