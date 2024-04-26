package ru.mal.unialbumsbackend.web.controller;

import io.minio.MinioClient;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.mal.unialbumsbackend.domain.User;
import ru.mal.unialbumsbackend.web.dto.UniverseResponse;
import ru.mal.unialbumsbackend.exception.ImageUploadException;
import ru.mal.unialbumsbackend.repositories.UserRepository;
import ru.mal.unialbumsbackend.service.ImageService;
import ru.mal.unialbumsbackend.service.UserService;

import java.util.Optional;

import static ru.mal.unialbumsbackend.web.controller.AlbumsController.decodeJWTGetHeader;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class ImageController {

    private final ImageService imageService;

    private final UserService userService;

    private final UserRepository userRepository;

    @PostMapping("/addAvatar")
    public void addAvatar(@RequestHeader("Authorization") String jwt ,@RequestParam("avatar") MultipartFile avatar
    ) {

        JSONObject jsonObject = decodeJWTGetHeader(jwt);

        long userId = ((Number) jsonObject.get("userId")).longValue();



        String filename= imageService.upload(avatar);

        Optional<User> user=userService.findById(userId);
        if (user.isPresent()) {
//            user.get().setAvatar("http://localhost:9000/images/" + filename);
            user.get().setAvatar("http://79.174.95.140:9000/images"+filename);
            userRepository.save(user.get());
        }

    }

    @ExceptionHandler
    private ResponseEntity<UniverseResponse> handleException(ImageUploadException e){
        UniverseResponse universeResponse=new UniverseResponse();
        universeResponse.setMessage("Не удалось загрузить изображение");
        return new ResponseEntity<>(universeResponse, HttpStatus.NOT_FOUND);
    }


}
