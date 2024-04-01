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

import java.util.ArrayList;
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
        response.setData(new ArrayList<>());
        long userId = ((Number)jsonObject.get("userId")).longValue();
        albumService.create(request,userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getByUserId")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UniverseResponse> getAllAlbums(@RequestHeader(name = "Authorization") String jwt){
        JSONObject jsonObject = decodeJWTGetHeader(jwt);

        long userId = ((Number) (Object) jsonObject.get("userId")).longValue();

        System.out.println("userId:"+userId);

         List<AlbumResponse> albums= albumService.getAlbumsByUserId(userId);

         UniverseResponse universeResponse=new UniverseResponse();
         universeResponse.setData(new ArrayList<>());
         universeResponse.setMessage("user's albums:");

        for(int i=0;i<albums.size();i++) {
            HashMap<String,String> map=new HashMap<>();
            universeResponse.addMap(map);
            universeResponse.addData(map, "title", albums.get(i).getTitle());
            universeResponse.addData(map, "cover", albums.get(i).getCover());
            universeResponse.addData(map, "tracksRating", Double.toString(albums.get(i).getTracksRating()));
            universeResponse.addData(map, "atmosphereRating", Double.toString(albums.get(i).getAtmosphereRating()));
            universeResponse.addData(map, "bitsRating", Double.toString(albums.get(i).getBitsRating()));
            universeResponse.addData(map, "textRating", Double.toString(albums.get(i).getTextRating()));
            universeResponse.addData(map, "artist", albums.get(i).getArtist());
        }
         return ResponseEntity.ok(universeResponse);

    }


//    [
//    {"title":"fgh"},
//    {"title":"uyi"},
//            ]

    public JSONObject decodeJWTGetHeader(String jwt){
        jwt= jwt.replace("Bearer ", "");
        String[] chunks=jwt.split("\\.");

        Base64.Decoder decoder=Base64.getUrlDecoder();
        String payload=new String(decoder.decode(chunks[1]));
        return new JSONObject(payload);
    }

}
