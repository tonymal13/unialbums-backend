package ru.mal.unialbumsbackend.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.mal.unialbumsbackend.domain.requests.CreateAlbumRequest;
import ru.mal.unialbumsbackend.domain.response.AlbumResponse;
import ru.mal.unialbumsbackend.domain.response.UniverseResponse;
import ru.mal.unialbumsbackend.service.AlbumService;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

@RequestMapping("/albums")
@RestController
public class AlbumsController {

    private final AlbumService albumService;


    @Value("{jwt.secret.access}")
    private String secret;

    public AlbumsController(AlbumService albumService) {

        this.albumService = albumService;
    }

    @PostMapping("/create")
    public ResponseEntity<UniverseResponse> create(@RequestHeader(name = "Authorization") String jwt, @RequestBody CreateAlbumRequest request)
    {
        JSONObject jsonObject = decodeJWTGetHeader(jwt);
        UniverseResponse response=new UniverseResponse();
        response.setMessage("album_created");
        response.setData(new ArrayList<>());
        long userId = ((Number)jsonObject.get("userId")).longValue();
        albumService.create(request,userId);
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

    public JSONObject decodeJWTGetHeader(String jwt){
        jwt= jwt.replace("Bearer ", "");
        String[] chunks=jwt.split("\\.");

        Base64.Decoder decoder=Base64.getUrlDecoder();
        String payload=new String(decoder.decode(chunks[1]));
        return new JSONObject(payload);
    }

}
