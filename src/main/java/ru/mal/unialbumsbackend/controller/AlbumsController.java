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
import ru.mal.unialbumsbackend.service.UserService;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;

@RequestMapping("/albums")
@RestController
public class AlbumsController {

    private final AlbumService albumService;

    private final UserService userService;

    @Value("{jwt.secret.access}")
    private String secret;

    public AlbumsController(AlbumService albumService, UserService userService) {

        this.albumService = albumService;
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<UniverseResponse> create(@RequestHeader(name = "Authorization") String jwt, @RequestBody CreateAlbumRequest request)
    {
        JSONObject jsonObject = decodeJWTGetHeader(jwt);
        UniverseResponse response=new UniverseResponse();
        response.setMessage("album_created");
        response.setData(new HashMap<>());
        long userId = ((Number)jsonObject.get("userId")).longValue();
        albumService.create(request,userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getByUserId")
    @ResponseStatus(HttpStatus.OK)
    public List<AlbumResponse> getAllAlbums(@RequestHeader(name = "Authorization") String jwt){
        JSONObject jsonObject = decodeJWTGetHeader(jwt);

        long userId = ((Number) (Object) jsonObject.get("userId")).longValue();

         return albumService.getAlbumsByUserId(userId);
    }

    public JSONObject decodeJWTGetHeader(String jwt){
        jwt= jwt.replace("Bearer ", "");
        String[] chunks=jwt.split("\\.");

        Base64.Decoder decoder=Base64.getUrlDecoder();
        String payload=new String(decoder.decode(chunks[1]));
        return new JSONObject(payload);
    }

}
