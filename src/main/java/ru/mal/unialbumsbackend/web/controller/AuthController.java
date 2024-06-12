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

        BackendResponse tokens = authService.login(authRequest);

        String refreshToken=tokens.getData().get(0).get("refreshToken");

        BackendResponse backendResponse = initializeResponse();
        backendResponse.setMessage("Вы вошли в аккаунт");

        HashMap<String,String> map = new HashMap<>();
        backendResponse.addMap(map);
        backendResponse.addData(map,"accessToken",tokens.getData().get(0).get("accessToken"));


        Cookie cookie=new Cookie("refreshToken",refreshToken);
        sendRefreshToken(cookie,response);

        return ResponseEntity.ok(backendResponse);
    }

    @PostMapping("/token")
    public ResponseEntity<BackendResponse> getNewAccessToken(@RequestBody RefreshJwtDto request) {
        final BackendResponse token = authService.getAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    @GetMapping("/refresh")
    public ResponseEntity<BackendResponse> getNewRefreshToken(@CookieValue(name = "refreshToken", required = false) String refreshToken, HttpServletResponse response) {

        final BackendResponse backendResponse = authService.refresh(refreshToken);
        String newRefreshToken= backendResponse.getData().get(0).get("refreshToken");
        Cookie cookie=new Cookie("refreshToken",newRefreshToken);
        sendRefreshToken(cookie,response);
        backendResponse.removeFromData("refreshToken");
        return ResponseEntity.ok(backendResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDto request) {
        BackendResponse backendResponse = initializeResponse();
        String message =userValidator.validateForRegister(request);
        if (message.equals("Вы успешно зарегестрировались")){
            userService.register(request);
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
