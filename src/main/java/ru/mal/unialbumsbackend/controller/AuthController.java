package ru.mal.unialbumsbackend.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mal.unialbumsbackend.domain.User;
import ru.mal.unialbumsbackend.domain.requests.LogInRequest;
import ru.mal.unialbumsbackend.domain.requests.RefreshJwtRequest;
import ru.mal.unialbumsbackend.domain.requests.RegRequest;
import ru.mal.unialbumsbackend.domain.response.UniverseResponse;
import ru.mal.unialbumsbackend.exception.AuthException;
import ru.mal.unialbumsbackend.service.AuthService;
import ru.mal.unialbumsbackend.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<UniverseResponse> login(@RequestBody LogInRequest authRequest, HttpServletResponse response) {

        System.out.println("Got a request");

        UniverseResponse tokens = authService.login(authRequest);

        String refreshToken=tokens.getData().get(0).get("refreshToken");

        UniverseResponse universeResponse=new UniverseResponse(
                );
        universeResponse.setData(new ArrayList<>());
        universeResponse.setMessage("Вы вошли в аккаунт");




        HashMap<String,String> map = new HashMap<>();
        universeResponse.addMap(map);
        universeResponse.addData(map,"accessToken",tokens.getData().get(0).get("accessToken"));


        Cookie cookie=new Cookie("refreshToken",refreshToken);
        sendRefreshToken(cookie,response);

        return ResponseEntity.ok(universeResponse);
    }

    @PostMapping("/token")
    public ResponseEntity<UniverseResponse> getNewAccessToken(@RequestBody RefreshJwtRequest request) {
        final UniverseResponse token = authService.getAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    @GetMapping("/refresh")
    public ResponseEntity<UniverseResponse> getNewRefreshToken(@CookieValue(name = "refreshToken", required = false) String refreshToken,HttpServletResponse response) {

        final UniverseResponse universeResponse = authService.refresh(refreshToken);
        String newRefreshToken= universeResponse.getData().get(0).get("refreshToken");
        Cookie cookie=new Cookie("refreshToken",newRefreshToken);
        sendRefreshToken(cookie,response);
        universeResponse.removeFromData("refreshToken");
        return ResponseEntity.ok(universeResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<UniverseResponse> register(@RequestBody RegRequest request) {
        UniverseResponse response = new UniverseResponse();
        response.setData(new ArrayList<>());
        String message = validate(request);
        if (message.equals("Добавлено в БД")){
            userService.register(request);
        }
            response.setMessage(message);
            return ResponseEntity.ok(response);

    }

        @ExceptionHandler
    private ResponseEntity<UniverseResponse> handleException(AuthException e){
        UniverseResponse universeResponse=new UniverseResponse();
        universeResponse.setMessage("Пользователь не найден");
        return new ResponseEntity<>(universeResponse, HttpStatus.NOT_FOUND);
    }

    private void sendRefreshToken(Cookie cookie ,HttpServletResponse response) {

        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setDomain(null);
        cookie.setMaxAge(30*24*60*60);

        response.addCookie(cookie);
    }

    private String validate(RegRequest request){
        String message= "";
        Optional<User> user=userService.findByLogin(request.getLogin());
        if (user.isPresent()) {
            message= ("Такой пользователь уже существует");
        } else {
           message= ("Добавлено в БД");
        }
        String regex = "\\p{Lu}\\p{L}{1,20}";


        if(request.getPassword().length()<1||request.getPassword().length()>20)
            message="Пароль должен быть от 1 до 20 символов :)";
        else if(request.getLogin().length()<1)
            message="Логин должен больше 1 до 20 символов :)";
        else if(!request.getFirstName().matches(regex))
            message="Имя должно быть в формате: Иван";
        else if(!request.getLastName().matches(regex))
            message="Фамилия должна быть в формате: Иванов";
        return message;
    }

    @GetMapping("/test")
    public String test(){
        return "It's ok";
    }

}
