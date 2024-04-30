package ru.mal.unialbumsbackend.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.mal.unialbumsbackend.domain.User;
import ru.mal.unialbumsbackend.exception.ImageUploadException;
import ru.mal.unialbumsbackend.exception.ValidationException;
import ru.mal.unialbumsbackend.repositories.UserRepository;
import ru.mal.unialbumsbackend.service.ImageService;
import ru.mal.unialbumsbackend.service.UserService;
import ru.mal.unialbumsbackend.util.UserValidator;
import ru.mal.unialbumsbackend.web.dto.UniverseResponse;
import ru.mal.unialbumsbackend.web.dto.auth.UserDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import static ru.mal.unialbumsbackend.web.controller.AlbumsController.decodeJWTGetHeader;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
@Tag(name = "User Controller",description = "User API")
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
        HashMap<String,String> map= new HashMap<>();
        response.addMap(map);

        long userId = ((Number)jsonObject.get("userId")).longValue();

        Optional<User> user=userService.findById(userId);
        getInfo(response,map,user);
        response.addData(map, "firstName",user.get().getFirstName());
        response.addData(map,"lastName",user.get().getLastName());
        if(response.getMessage().equals("Данные пользователя")){
            return ResponseEntity.ok(response);
        }
        else{
            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("/myProfile")
    public ResponseEntity<UniverseResponse> editProfile(@RequestHeader("Authorization") String jwt, @RequestBody UserDto request) {
        JSONObject jsonObject = decodeJWTGetHeader(jwt);
        UniverseResponse response=new UniverseResponse();

        response.setData(new ArrayList<>());

        long userId = ((Number)jsonObject.get("userId")).longValue();

        Optional<User> user=userService.findById(userId);

        String message= userValidator.validateForEdit(request,userId);

        if (message.equals("Данные успешно обновлены")){
            response.setMessage(message);
            userService.toDto(user.get(),request);
            userService.save(user.get());
            return ResponseEntity.ok(response);
        }
        else if(message.equals("Пароль должен быть от 1 до 20 символов :)")||message.equals("Логин должен быть от 1 до 20 символов :)")) {
            response.setMessage(message);
            return new ResponseEntity<UniverseResponse>(response,HttpStatus.BAD_REQUEST);
        }
        else{
            response.setMessage(message);
            return new ResponseEntity<UniverseResponse>(response,HttpStatus.NOT_FOUND);
        }

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

    @GetMapping("/getUserInfo")
    public ResponseEntity<UniverseResponse> getUserInfo(@RequestHeader("Authorization") String jwt){
        JSONObject jsonObject = decodeJWTGetHeader(jwt);
        UniverseResponse response=new UniverseResponse();

        response.setData(new ArrayList<>());

        long userId = ((Number)jsonObject.get("userId")).longValue();

        Optional<User> user=userService.findById(userId);
        HashMap<String,String> map= new HashMap<>();
        response.addMap(map);
        getInfo(response,map,user);
        if(response.getMessage().equals("Данные пользователя")){
            return ResponseEntity.ok(response);
        }
        else{
            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
        }
    }


    public void getInfo(UniverseResponse response, HashMap<String,String> map, Optional<User> user){

        if (user.isPresent()){
            response.addData(map, "username", user.get().getUsername());
            response.addData(map,"avatar",user.get().getAvatar());
            response.setMessage("Данные пользователя");
        }
        else{
            response.setMessage("Пользователь не найден");
        }
    }

}
