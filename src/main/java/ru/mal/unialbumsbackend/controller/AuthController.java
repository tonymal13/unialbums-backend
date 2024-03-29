package ru.mal.unialbumsbackend.controller;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mal.unialbumsbackend.domain.*;
import ru.mal.unialbumsbackend.repositories.UserRepository;
import ru.mal.unialbumsbackend.service.AuthService;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final UserRepository repository;

    @PostMapping("/login")
    public ResponseEntity<AccessTokenResponse> login(@RequestBody LogInRequest authRequest, HttpServletResponse response) {
        LogInResponse tokens = authService.login(authRequest);
        AccessTokenResponse accessToken=new AccessTokenResponse(tokens.getAccessToken());
        String refreshToken=tokens.getRefreshToken();
        Cookie cookie=new Cookie("refresh_token",refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setDomain(null);
        cookie.setMaxAge(30*24*60*60);

        response.addCookie(cookie);
        return ResponseEntity.ok(accessToken);
    }

    @PostMapping("/token")
    public ResponseEntity<LogInResponse> getNewAccessToken(@RequestBody RefreshJwtRequest request) {
        final LogInResponse token = authService.getAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LogInResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) {
        final LogInResponse token = authService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    public ResponseEntity<RegResponse> register(@RequestBody LogInRequest request)
    {
        RegResponse response=new RegResponse();
        response.setMessage("added to db");
        User user=new User();
        user.setRole("USER");
        user.setPassword(request.getPassword());
        user.setLogin(request.getLogin());
        user.setLastName("aaaa");
        user.setFirstName("bbbbbb");
       repository.save(user);
        return ResponseEntity.ok(response);
    }


}
