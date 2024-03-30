package ru.mal.unialbumsbackend.controller;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.mal.unialbumsbackend.domain.Album;
import ru.mal.unialbumsbackend.domain.User;
import ru.mal.unialbumsbackend.domain.requests.CreateAlbumRequest;
import ru.mal.unialbumsbackend.domain.requests.RegRequest;
import ru.mal.unialbumsbackend.domain.response.CreatedResponse;
import ru.mal.unialbumsbackend.service.AlbumService;
import ru.mal.unialbumsbackend.service.UserService;

import java.util.List;
import java.util.Optional;

@RequestMapping("/albums")
@AllArgsConstructor
@RestController
public class AlbumsController {

    private final AlbumService albumService;

    private final UserService userService;

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
    public List<Album> getAllAlbums(@RequestHeader(name = "Authorization")String jwt){

//        System.out.println(jwt);
//        String currentPrincipalName=getPrincipalName();
//        Optional<User> user=userService.findBuFullName(currentPrincipalName);

        return albumService.getAllAlbums();
    }

    public Authentication getAuth(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public String getPrincipalName(){
        return getAuth().getName();
    }

}
