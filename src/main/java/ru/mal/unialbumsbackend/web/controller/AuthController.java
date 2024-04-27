package ru.mal.unialbumsbackend.web.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mal.unialbumsbackend.util.UserValidator;
import ru.mal.unialbumsbackend.web.dto.auth.LogInRequest;
import ru.mal.unialbumsbackend.web.dto.auth.RefreshJwtRequest;
import ru.mal.unialbumsbackend.web.dto.auth.RegRequest;
import ru.mal.unialbumsbackend.web.dto.UniverseResponse;
import ru.mal.unialbumsbackend.exception.AuthException;
import ru.mal.unialbumsbackend.service.AuthService;
import ru.mal.unialbumsbackend.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final UserValidator userValidator;

    private final AuthService authService;

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<UniverseResponse> login(@RequestBody LogInRequest authRequest, HttpServletResponse response) {

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
        String message =userValidator.validateForRegister(request);
        if (message.equals("Добавлено в БД")){
            userService.register(request);
        }
            response.setMessage(message);
            return ResponseEntity.ok(response);
    }

        @ExceptionHandler
    private ResponseEntity<UniverseResponse> handleException(AuthException e){
        UniverseResponse universeResponse=new UniverseResponse();
        universeResponse.setMessage( "Пользователь не найден");
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
    @GetMapping("/test")
    public String test(){
        return "Made by Tonymal13 and def1s";
    }

}
