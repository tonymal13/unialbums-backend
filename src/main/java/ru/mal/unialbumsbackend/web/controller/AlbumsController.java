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
import ru.mal.unialbumsbackend.web.dto.UniverseResponse;
import ru.mal.unialbumsbackend.service.AlbumService;
import ru.mal.unialbumsbackend.service.ImageService;

import java.util.*;

import static ru.mal.unialbumsbackend.web.dto.UniverseResponse.initializeResponse;
import static ru.mal.unialbumsbackend.web.security.JwtUtils.decodeJWTGetHeader;

@RequestMapping("/api/v1/albums")
@RestController
@AllArgsConstructor
@Tag(name = "Albums Controller",description = "Albums API")
public class AlbumsController {

    private final AlbumService albumService;

    private final ImageService imageService;

    @PostMapping("/create")
    public ResponseEntity<UniverseResponse> create(@RequestHeader(name = "Authorization") String jwt, @ModelAttribute("request") CreateAlbumDto request
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

         List<AlbumDto> albums= albumService.getAlbumsByUserId(userId);

         UniverseResponse universeResponse=initializeResponse();
         universeResponse.setMessage("Альбомы пользователя:");

        for (AlbumDto album : albums) {
            addData(universeResponse,album);

        }
         return ResponseEntity.ok(universeResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity editAlbumInfo(@PathVariable("id") long albumId, @RequestBody CreateAlbumDto req, @RequestHeader(name = "Authorization") String jwt){

        AlbumDto albumDto = findAlbum(jwt, albumId);

        if(albumDto !=null){
            UniverseResponse universeResponse = initializeResponse();
            Optional<Album> album =albumService.findById(albumId);
            edit(album.get(),req);
            albumService.save(album.get());
            universeResponse.setMessage("Данные успешно обновлены");
            return ResponseEntity.ok(universeResponse);
        }
        else{
            UniverseResponse universeResponse = new UniverseResponse();
            universeResponse.setMessage("Вы не можете получить доступ к этому альбому:");
            return new ResponseEntity(universeResponse,HttpStatus.BAD_REQUEST);
        }

    }

    private void edit(Album album, CreateAlbumDto req){
        album.setTitle(req.getTitle());
        album.setArtist(req.getArtist());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UniverseResponse> getAlbumInfo(@PathVariable("id") long albumId,@RequestHeader(name = "Authorization") String jwt){

        UniverseResponse universeResponse = initializeResponse();

        AlbumDto albumDto = findAlbum(jwt, albumId);

        if(albumDto !=null){

            addData(universeResponse, albumDto);
            universeResponse.setMessage("Информация об альбоме:");
        }
        else{
            universeResponse = new UniverseResponse();
            universeResponse.setMessage("Вы не можете получить доступ к этому альбому:");
        }
        return ResponseEntity.ok(universeResponse);
    }

    private AlbumDto findAlbum(String jwt, long albumId) {
        JSONObject jsonObject = decodeJWTGetHeader(jwt);

        long userId = ((Number) jsonObject.get("userId")).longValue();

        List<AlbumDto> albums=albumService.getAlbumsByUserId(userId);

        AlbumDto albumDto =null;
        for(AlbumDto album:albums){
            if(album.getId()==albumId)
                albumDto =album;
        }

        return albumDto;
    }

    public void addData(UniverseResponse universeResponse, AlbumDto albumDto){
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
