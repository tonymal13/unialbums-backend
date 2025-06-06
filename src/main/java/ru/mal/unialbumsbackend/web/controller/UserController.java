package ru.mal.unialbumsbackend.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.mal.unialbumsbackend.domain.User;
import ru.mal.unialbumsbackend.repositories.UserRepository;
import ru.mal.unialbumsbackend.service.ImageService;
import ru.mal.unialbumsbackend.service.UserService;
import ru.mal.unialbumsbackend.util.UserValidator;
import ru.mal.unialbumsbackend.web.dto.BackendResponse;
import ru.mal.unialbumsbackend.web.dto.auth.UserDto;

import java.util.HashMap;

import static ru.mal.unialbumsbackend.config.WebConfig.host;
import static ru.mal.unialbumsbackend.web.dto.BackendResponse.initializeResponse;
import static ru.mal.unialbumsbackend.web.security.JwtUtils.decodeJWTGetHeader;

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
    public ResponseEntity<BackendResponse> getProfile(@RequestHeader(name = "Authorization") String jwt){
        JSONObject jsonObject = decodeJWTGetHeader(jwt);
        BackendResponse backendResponse = initializeResponse();
        HashMap<String,String> map= new HashMap<>();
        backendResponse.addMap(map);

        long userId = ((Number)jsonObject.get("userId")).longValue();

        User user=userService.findById(userId);
        addInfo(backendResponse,map,user);
        backendResponse.addData(map, "firstName",user.getFirstName());
        backendResponse.addData(map,"lastName",user.getLastName());
        if(backendResponse.getMessage().equals("Данные пользователя")){
            return ResponseEntity.ok(backendResponse);
        }
        else{
            return new ResponseEntity<>(backendResponse,HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("/myProfile")
    public ResponseEntity<?> editProfile(@RequestHeader("Authorization") String jwt, @ModelAttribute("request") UserDto userDto,
            @RequestParam(value = "avatar",required = false) Object avatar){
        JSONObject jsonObject = decodeJWTGetHeader(jwt);
        BackendResponse backendResponse = initializeResponse();

        long userId = ((Number)jsonObject.get("userId")).longValue();

        User user=userService.findById(userId);

        String message= userValidator.validateForEdit(userDto);

        if (message.equals("Данные успешно обновлены")){
            backendResponse.setMessage(message);
            userService.toDto(user,userDto);
            userService.addAvatarToUser(user,avatar);
            userService.save(user);
            return ResponseEntity.ok(backendResponse);
        }
        else if(message.equals("Пароль должен быть от 1 до 20 символов :)")||message.equals("Логин должен быть от 1 до 20 символов :)")) {
            backendResponse.setMessage(message);
            return new ResponseEntity<>(backendResponse,HttpStatus.BAD_REQUEST);
        }
        else{
            backendResponse.setMessage(message);
            return new ResponseEntity<>(backendResponse,HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/addAvatar")
    public void addAvatar(@RequestHeader("Authorization") String jwt ,@RequestParam("avatar") MultipartFile avatar
    ) {

        JSONObject jsonObject = decodeJWTGetHeader(jwt);

        long userId = ((Number) jsonObject.get("userId")).longValue();

        String filename= imageService.upload(avatar);

        User user=userService.findById(userId);
        user.setAvatar(host+":9000/images/"+filename);
        userRepository.save(user);
    }

    @GetMapping("/getUserInfo")
    public ResponseEntity<BackendResponse> getUserInfo(@RequestHeader("Authorization") String jwt){
        JSONObject jsonObject = decodeJWTGetHeader(jwt);
        BackendResponse backendResponse = initializeResponse();

        long userId = ((Number)jsonObject.get("userId")).longValue();

        User user=userService.findById(userId);
        HashMap<String,String> map= new HashMap<>();
        backendResponse.addMap(map);
        addInfo(backendResponse,map,user);
        if(backendResponse.getMessage().equals("Данные пользователя")){
            return ResponseEntity.ok(backendResponse);
        }
        else{
            return new ResponseEntity<>(backendResponse,HttpStatus.NOT_FOUND);
        }
    }

    public void addInfo(BackendResponse response, HashMap<String,String> map, User user){

            response.addData(map, "username", user.getUsername());
            response.addData(map,"avatar",user.getAvatar());
            response.setMessage("Данные пользователя");

    }

}
