package ru.mal.unialbumsbackend.controller;

import io.minio.MinioClient;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.mal.unialbumsbackend.domain.User;
import ru.mal.unialbumsbackend.repositories.UserRepository;
import ru.mal.unialbumsbackend.service.ImageService;
import ru.mal.unialbumsbackend.service.UserService;

import java.util.Optional;

import static ru.mal.unialbumsbackend.controller.AlbumsController.decodeJWTGetHeader;

@RestController
public class ImageController {

    private final ImageService imageService;

    private final UserService userService;

    private final UserRepository userRepository;

    private final MinioClient minioClient;

    public ImageController(ImageService imageService, UserService userService, UserRepository userRepository, MinioClient minioClient) {
        this.imageService = imageService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.minioClient = minioClient;
    }


    @PostMapping("/addAvatar")
    public void addAvatar(@RequestHeader("Authorization") String jwt ,@RequestParam("avatar") MultipartFile avatar
    ) {

        JSONObject jsonObject = decodeJWTGetHeader(jwt);

        long userId = ((Number) jsonObject.get("userId")).longValue();



        String filename= imageService.upload(avatar);

        Optional<User> user=userService.findById(userId);
        if (user.isPresent()) {
            user.get().setAvatar("http://localhost:9000/images/" + filename);
            userRepository.save(user.get());
        }

        System.out.println(avatar.getName());
        System.out.println(avatar.getOriginalFilename());

    }


}
