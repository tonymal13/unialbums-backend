package ru.mal.unialbumsbackend.controller;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mal.unialbumsbackend.domain.JwtRequest;
import ru.mal.unialbumsbackend.domain.AccessTokenResponse;
import ru.mal.unialbumsbackend.domain.JwtResponse;
import ru.mal.unialbumsbackend.domain.RefreshJwtRequest;
import ru.mal.unialbumsbackend.service.AuthService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AccessTokenResponse> login(@RequestBody JwtRequest authRequest, HttpServletResponse response) {
        JwtResponse tokens = authService.login(authRequest);
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
    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody RefreshJwtRequest request) {
        final JwtResponse token = authService.getAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) {
        final JwtResponse token = authService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    @GetMapping("/svo")
    public String  svo(){
        return "svo";
    }

}
