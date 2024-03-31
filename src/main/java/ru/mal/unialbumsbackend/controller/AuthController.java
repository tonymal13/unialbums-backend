package ru.mal.unialbumsbackend.controller;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mal.unialbumsbackend.domain.*;
import ru.mal.unialbumsbackend.domain.requests.LogInRequest;
import ru.mal.unialbumsbackend.domain.requests.RefreshJwtRequest;
import ru.mal.unialbumsbackend.domain.requests.RegRequest;
import ru.mal.unialbumsbackend.domain.response.TokensResponse;
import ru.mal.unialbumsbackend.domain.response.UniverseResponse;
import ru.mal.unialbumsbackend.repositories.UserRepository;
import ru.mal.unialbumsbackend.service.AuthService;
import ru.mal.unialbumsbackend.service.UserService;
import ru.mal.unialbumsbackend.util.UserNotFoundException;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<UniverseResponse> login(@RequestBody LogInRequest authRequest, HttpServletResponse response) {
        UniverseResponse tokens = authService.login(authRequest);

        String refreshToken=tokens.getData().get("refreshToken");

        UniverseResponse universeResponse=new UniverseResponse(
                );
        universeResponse.setData(new HashMap<>());
        universeResponse.setMessage("logged in");
        universeResponse.addData("accessToken",tokens.getData().get("accessToken"));


        Cookie cookie=new Cookie("refreshToken",refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setDomain(null);
        cookie.setMaxAge(30*24*60*60);

        response.addCookie(cookie);

        return ResponseEntity.ok(universeResponse);
    }

    @PostMapping("/token")
    public ResponseEntity<UniverseResponse> getNewAccessToken(@RequestBody RefreshJwtRequest request) {
        final UniverseResponse token = authService.getAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokensResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) {
        final TokensResponse token = authService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    public ResponseEntity<UniverseResponse> register(@RequestBody RegRequest request)
    {
        UniverseResponse response=new UniverseResponse();
        response.setMessage("added to db");
        response.setData(new HashMap<>());

       userService.register(request);
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler
    private ResponseEntity<UniverseResponse> handleException(UserNotFoundException e){
        UniverseResponse universeResponse=new UniverseResponse();
        universeResponse.setMessage("User not found");
        return new ResponseEntity<>(universeResponse, HttpStatus.NOT_FOUND);
    }

}
