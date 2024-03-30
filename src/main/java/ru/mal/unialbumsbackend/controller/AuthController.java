package ru.mal.unialbumsbackend.controller;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mal.unialbumsbackend.domain.*;
import ru.mal.unialbumsbackend.domain.requests.LogInRequest;
import ru.mal.unialbumsbackend.domain.requests.RefreshJwtRequest;
import ru.mal.unialbumsbackend.domain.requests.RegRequest;
import ru.mal.unialbumsbackend.domain.response.AccessTokenResponse;
import ru.mal.unialbumsbackend.domain.response.CreatedResponse;
import ru.mal.unialbumsbackend.domain.response.TokensResponse;
import ru.mal.unialbumsbackend.repositories.UserRepository;
import ru.mal.unialbumsbackend.service.AuthService;
import ru.mal.unialbumsbackend.service.UserService;

import java.security.SecureRandom;
import java.util.Base64;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final UserService userService;

//    private String generateSafeToken() {
//        SecureRandom random = new SecureRandom();
//        byte[] bytes = new byte[38]; // 36 bytes * 8 = 288 bits, a little bit more than
//        // the 256 required bits
//        random.nextBytes(bytes);
//        var encoder = Base64.getUrlEncoder().withoutPadding();
//        return encoder.encodeToString(bytes);
//    }

    @PostMapping("/login")
    public ResponseEntity<AccessTokenResponse> login(@RequestBody LogInRequest authRequest, HttpServletResponse response) {
        TokensResponse tokens = authService.login(authRequest);

        String refreshToken=tokens.getRefreshToken();

        AccessTokenResponse accessTokenResponse=new AccessTokenResponse(
                tokens.getAccessToken());

        Cookie cookie=new Cookie("refresh_token",refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setDomain(null);
        cookie.setMaxAge(30*24*60*60);

        response.addCookie(cookie);

//        System.out.println(generateSafeToken());

        return ResponseEntity.ok(accessTokenResponse);
    }

    @PostMapping("/token")
    public ResponseEntity<TokensResponse> getNewAccessToken(@RequestBody RefreshJwtRequest request) {
        final TokensResponse token = authService.getAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokensResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) {
        final TokensResponse token = authService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    public ResponseEntity<CreatedResponse> register(@RequestBody RegRequest request)
    {
        CreatedResponse response=new CreatedResponse();
        response.setMessage("added to db");

       userService.register(request);
        return ResponseEntity.ok(response);
    }



}
