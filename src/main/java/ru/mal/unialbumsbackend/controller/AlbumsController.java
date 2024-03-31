package ru.mal.unialbumsbackend.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.mal.unialbumsbackend.domain.Album;
import ru.mal.unialbumsbackend.domain.User;
import ru.mal.unialbumsbackend.domain.requests.CreateAlbumRequest;
import ru.mal.unialbumsbackend.domain.response.AlbumResponse;
import ru.mal.unialbumsbackend.domain.response.CreatedResponse;
import ru.mal.unialbumsbackend.service.AlbumService;
import ru.mal.unialbumsbackend.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<CreatedResponse> create(@RequestHeader(name = "Authorization") String jwt, @RequestBody CreateAlbumRequest request)
    {
        JSONObject jsonObject = decodeJWTgetHeader(jwt);
        CreatedResponse response=new CreatedResponse();
        response.setMessage("album_created");
        long userId = ((Number)jsonObject.get("userId")).longValue();
        albumService.create(request,userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getByUserId")
    @ResponseStatus(HttpStatus.OK)
    public List<AlbumResponse> getAllAlbums(@RequestHeader(name = "Authorization") String jwt){
        JSONObject jsonObject = decodeJWTgetHeader(jwt);

        long userId = ((Number) (Object) jsonObject.get("userId")).longValue();

         return albumService.getAlbumsByUserId(userId);
    }

    public JSONObject decodeJWTgetHeader(String jwt){
        jwt= jwt.replace("Bearer ", "");
        String[] chunks=jwt.split("\\.");

        Base64.Decoder decoder=Base64.getUrlDecoder();
        String payload=new String(decoder.decode(chunks[1]));
        return new JSONObject(payload);
    }

}
