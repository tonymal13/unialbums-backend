package ru.mal.unialbumsbackend.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mal.unialbumsbackend.util.UserValidator;
import ru.mal.unialbumsbackend.web.dto.auth.LogInDto;
import ru.mal.unialbumsbackend.web.dto.auth.RefreshAndAccessDto;
import ru.mal.unialbumsbackend.web.dto.auth.RefreshJwtDto;
import ru.mal.unialbumsbackend.web.dto.auth.UserDto;
import ru.mal.unialbumsbackend.web.dto.BackendResponse;
import ru.mal.unialbumsbackend.service.AuthService;
import ru.mal.unialbumsbackend.service.UserService;

import java.util.HashMap;

import static ru.mal.unialbumsbackend.web.dto.BackendResponse.initializeResponse;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Auth Controller",description = "Auth API")
public class AuthController {

    private final UserValidator userValidator;

    private final AuthService authService;

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<BackendResponse> login(@RequestBody LogInDto authRequest, HttpServletResponse response) {

        RefreshAndAccessDto tokens= authService.login(authRequest);

        String refreshToken=tokens.getRefreshToken();

        BackendResponse backendResponse = initializeResponse();
        backendResponse.setMessage("Вы вошли в аккаунт");

        HashMap<String,String> map = new HashMap<>();
        backendResponse.addMap(map);
        backendResponse.addData(map,"accessToken",tokens.getAccessToken());


        Cookie cookie=new Cookie("refreshToken",refreshToken);
        sendRefreshToken(cookie,response);

        return ResponseEntity.ok(backendResponse);
    }

    @PostMapping("/token")
    public ResponseEntity<BackendResponse> getNewAccessToken(@RequestBody RefreshJwtDto refreshJwtDto) {
        final RefreshAndAccessDto tokens = authService.getAccessToken(refreshJwtDto.getRefreshToken());
        BackendResponse backendResponse=initializeResponse();
        HashMap<String,String> map=new HashMap<>();
        backendResponse.addData(map,"accessToken", tokens.getAccessToken());
        backendResponse.setMessage("Токен обновлен");
        return ResponseEntity.ok(backendResponse);
    }

    @GetMapping("/refresh")
    public ResponseEntity<BackendResponse> getNewRefreshToken(@CookieValue(name = "refreshToken", required = false) String refreshToken, HttpServletResponse response) {

        final RefreshAndAccessDto tokens = authService.refresh(refreshToken);
        String newRefreshToken= tokens.getRefreshToken();
        Cookie cookie=new Cookie("refreshToken",newRefreshToken);
        sendRefreshToken(cookie,response);
        BackendResponse backendResponse=initializeResponse();
        backendResponse.setMessage("Токен обновлен");
        return ResponseEntity.ok(backendResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDto userDto) {
        BackendResponse backendResponse = initializeResponse();
        String message =userValidator.validateForRegister(userDto);
        if (message.equals("Вы успешно зарегистрировались")){
            userService.register(userDto);
            backendResponse.setMessage(message);
            return ResponseEntity.ok(backendResponse);
        }
        else {
            backendResponse.setMessage(message);
            return new ResponseEntity<>(backendResponse,HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/logout")
    public void getNewRefreshToken(HttpServletRequest req,HttpServletResponse resp) {

        Cookie[] cookies = req.getCookies();
        if (cookies != null)
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals("refreshToken")) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                }
                resp.addCookie(cookie);
            }
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
        return "Test1";
    }

}
