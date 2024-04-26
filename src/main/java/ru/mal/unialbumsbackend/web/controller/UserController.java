package ru.mal.unialbumsbackend.web.controller;

import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mal.unialbumsbackend.domain.User;
import ru.mal.unialbumsbackend.exception.AuthException;
import ru.mal.unialbumsbackend.service.ImageService;
import ru.mal.unialbumsbackend.service.UserService;
import ru.mal.unialbumsbackend.web.dto.UniverseResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import static ru.mal.unialbumsbackend.web.controller.AlbumsController.decodeJWTGetHeader;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/myProfile")
    public ResponseEntity<UniverseResponse> getProfile(@RequestHeader(name = "Authorization") String jwt){

        JSONObject jsonObject = decodeJWTGetHeader(jwt);
        UniverseResponse response=new UniverseResponse();

        response.setData(new ArrayList<>());
        long userId = ((Number)jsonObject.get("userId")).longValue();

        response.setData(new ArrayList<>());

        HashMap<String,String> map = new HashMap<>();
        response.addMap(map);

        Optional<User> user=userService.findById(userId);

        if (user.isPresent()){
            response.addData(map, "login", user.get().getLogin());
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

}
