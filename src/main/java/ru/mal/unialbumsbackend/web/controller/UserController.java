package ru.mal.unialbumsbackend.web.controller;

import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.mal.unialbumsbackend.domain.User;
import ru.mal.unialbumsbackend.exception.AuthException;
import ru.mal.unialbumsbackend.exception.ImageUploadException;
import ru.mal.unialbumsbackend.repositories.UserRepository;
import ru.mal.unialbumsbackend.service.ImageService;
import ru.mal.unialbumsbackend.service.UserService;
import ru.mal.unialbumsbackend.util.UserValidator;
import ru.mal.unialbumsbackend.web.dto.UniverseResponse;
import ru.mal.unialbumsbackend.web.dto.auth.RegRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static ru.mal.unialbumsbackend.web.controller.AlbumsController.decodeJWTGetHeader;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    private final ImageService imageService;

    private final UserRepository userRepository;

    private final UserValidator userValidator;

    @GetMapping("/myProfile")
    public ResponseEntity<UniverseResponse> getProfile(@RequestHeader(name = "Authorization") String jwt){

        JSONObject jsonObject = decodeJWTGetHeader(jwt);
        UniverseResponse response=new UniverseResponse();

        response.setData(new ArrayList<>());
        long userId = ((Number)jsonObject.get("userId")).longValue();

        HashMap<String,String> map = new HashMap<>();
        response.addMap(map);

        Optional<User> user=userService.findById(userId);

        if (user.isPresent()){
            response.addData(map, "username", user.get().getUsername());
            response.addData(map, "firstName",user.get().getFirstName());
            response.addData(map,"lastName",user.get().getLastName());
            response.addData(map,"avatar",user.get().getAvatar());
            response.setMessage("Данные пользователя");
        }

        else{
            response.setMessage("Пользователь не найден");
            throw new AuthException("Пользователь не найден");
        }


        return ResponseEntity.ok(response);

    }

    @PutMapping("/myProfile")
    public ResponseEntity<UniverseResponse> edit(@RequestHeader("Authorization") String jwt,@RequestBody RegRequest request) {
        JSONObject jsonObject = decodeJWTGetHeader(jwt);
        UniverseResponse response=new UniverseResponse();

        response.setData(new ArrayList<>());

        long userId = ((Number)jsonObject.get("userId")).longValue();

        Optional<User> user=userService.findById(userId);

        userService.edit(user.get(),request);

        String message =userValidator.validate(request);
        if (message.equals("Данные успешно обновлены")){
            userService.save(user.get());
        }
        response.setMessage(message);
        return ResponseEntity.ok(response);
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
//            user.get().setAvatar("http://79.174.95.140:9000/images/"+filename);
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