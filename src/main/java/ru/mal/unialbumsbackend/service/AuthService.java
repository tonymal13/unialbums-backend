package ru.mal.unialbumsbackend.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.mal.unialbumsbackend.exception.AuthException;
import ru.mal.unialbumsbackend.web.security.JwtProvider;
import ru.mal.unialbumsbackend.domain.*;
import ru.mal.unialbumsbackend.web.dto.auth.LogInDto;
import ru.mal.unialbumsbackend.web.dto.BackendResponse;
import ru.mal.unialbumsbackend.exception.UserNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.mal.unialbumsbackend.web.dto.BackendResponse.initializeResponse;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtProvider jwtProvider;

    private final PasswordEncoder passwordEncoder;

    public BackendResponse login(LogInDto logInDto) {
        User user = userService.findByUsername(logInDto.getUsername());
        BackendResponse backendResponse=initializeResponse();
            if (passwordEncoder.matches(logInDto.getPassword() ,user.getPassword())){
                final String accessToken = jwtProvider.generateAccessTokenForLogin(user);
                final String refreshToken = jwtProvider.generateRefreshToken(user);
                HashMap<String,String> map=new HashMap<>();
                backendResponse.addMap(map);
                backendResponse.addData(map,"accessToken",accessToken);
                backendResponse.addData(map,"refreshToken",refreshToken);
                backendResponse.setMessage("Вы зашли в аккаунт");
            } else {
                throw new UserNotFoundException("Пользователь не найден");
            }
        return backendResponse;
    }

    public BackendResponse getAccessToken(String refreshToken) {
        BackendResponse backendResponse=initializeResponse();
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            User user=userService.findByUsername(login);
            String accessToken = jwtProvider.generateAccessTokenForLogin(user);
            HashMap<String,String> map=new HashMap<>();
            backendResponse.setMessage("Токен: ");
            backendResponse.addData(map,"accessToken", accessToken);
        }
        return backendResponse;
    }

    public BackendResponse refresh(String refreshToken) {
        BackendResponse backendResponse=initializeResponse();
            if (jwtProvider.validateRefreshToken(refreshToken)) {
                final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
                final String login = claims.getSubject();
                User user = userService.findByUsername(login);
                final String accessToken = jwtProvider.generateAccessTokenForLogin(user);
                final String newRefreshToken = jwtProvider.generateRefreshToken(user);
                HashMap<String, String> map = new HashMap<>();
                backendResponse.setMessage("Токен обновлен");
                backendResponse.addMap(map);
                backendResponse.addData(map, "accessToken", accessToken);
                backendResponse.addData(map, "refreshToken", newRefreshToken);

                return backendResponse;
            }
        throw new AuthException("Невалидный JWT токен");
    }

//    public ResponseEntity<?> refresh(String refreshToken) {
//
//        if (jwtProvider.validateRefreshToken(refreshToken)) {
//            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
//            final String login = claims.getSubject();
//            User user = userService.findByUsername(login);
//            final String accessToken = jwtProvider.generateAccessTokenForLogin(user);
//            final String newRefreshToken = jwtProvider.generateRefreshToken(user);
//
//            return ResponseEntity.status(200).body(
//                    Map.of("data",
//                    List.of(
//                    Map.of(
//                    "accessToken",accessToken,"refreshToken",newRefreshToken,"message","Токен обновлен"))));
//        }
//        throw new AuthException("Невалидный JWT токен");
//    }

}
