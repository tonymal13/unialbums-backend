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
import ru.mal.unialbumsbackend.domain.requests.CreateAlbumRequest;
import ru.mal.unialbumsbackend.domain.response.CreatedResponse;
import ru.mal.unialbumsbackend.service.AlbumService;
import ru.mal.unialbumsbackend.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
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
    public ResponseEntity<CreatedResponse> create(@RequestBody CreateAlbumRequest request)
    {


        CreatedResponse response=new CreatedResponse();
        response.setMessage("album_created");

        albumService.create(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getByUserId")
    @ResponseStatus(HttpStatus.OK)
    public List<Album> getAllAlbums(@RequestHeader(name = "Authorization") String jwt){
        jwt= jwt.replace("Bearer ", "");
        String[] chunks=jwt.split("\\.");
        System.out.println(jwt);

        Base64.Decoder decoder=Base64.getUrlDecoder();
        String header=new String(decoder.decode(chunks[0]));
        String payload=new String(decoder.decode(chunks[1]));

        JSONObject jsonObject = new JSONObject(payload);


        long userId = ((Number) (Object) jsonObject.get("userId")).longValue();

        return albumService.getAlbumsByUserId(userId);
    }


}
