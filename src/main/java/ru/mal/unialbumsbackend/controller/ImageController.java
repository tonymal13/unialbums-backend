package ru.mal.unialbumsbackend.controller;

import io.minio.MinioClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.mal.unialbumsbackend.domain.User;
import ru.mal.unialbumsbackend.repositories.UserRepository;
import ru.mal.unialbumsbackend.service.ImageService;
import ru.mal.unialbumsbackend.service.UserService;

import java.util.Optional;

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


    @PostMapping("/image")
    public void uploadImage(@ModelAttribute MultipartFile image
    ) {
//        System.out.println("image"+image.getFile().getResource().getURL());
        String filename= imageService.upload(image);

        Optional<User> user=userService.findByLogin("a");
        if (user.isPresent())
            user.get().setAvatar("http://localhost:9000/images/Cats.jpg");
        userRepository.save(user.get());


        System.out.println(filename);
    }


}
