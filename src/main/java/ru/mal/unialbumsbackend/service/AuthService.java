package ru.mal.unialbumsbackend.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.mal.unialbumsbackend.exception.AuthException;
import ru.mal.unialbumsbackend.web.security.JwtProvider;
import ru.mal.unialbumsbackend.domain.*;
import ru.mal.unialbumsbackend.web.dto.auth.LogInDto;
import ru.mal.unialbumsbackend.web.dto.BackendResponse;
import ru.mal.unialbumsbackend.exception.UserNotFoundException;
import java.util.HashMap;

import static ru.mal.unialbumsbackend.web.dto.BackendResponse.initializeResponse;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtProvider jwtProvider;

    private final PasswordEncoder passwordEncoder;

    public BackendResponse login(LogInDto logInDto) {
        User user = userService.findByLogin(logInDto.getUsername());
        BackendResponse universeResponse=initializeResponse();
            if (passwordEncoder.matches(logInDto.getPassword() ,user.getPassword())){
                final String accessToken = jwtProvider.generateAccessTokenForLogin(user);
                final String refreshToken = jwtProvider.generateRefreshToken(user);
                HashMap<String,String> map=new HashMap<>();
                universeResponse.addMap(map);
                universeResponse.addData(map,"accessToken",accessToken);
                universeResponse.addData(map,"refreshToken",refreshToken);
                universeResponse.setMessage("Вы зашли в аккаунт");
            } else {
                throw new UserNotFoundException("Пользователь не найден");
            }
        return universeResponse;
    }

    public BackendResponse getAccessToken(String refreshToken) {
        BackendResponse universeResponse=initializeResponse();
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            if (jwtProvider.validateRefreshToken(refreshToken)) {
                User user=userService.findByLogin(login);
                String accessToken = jwtProvider.generateAccessTokenForLogin(user);
                HashMap<String,String> map=new HashMap<>();
                universeResponse.setMessage("Токен: ");
                universeResponse.addData(map,"accessToken", accessToken);
            }
        }
        return universeResponse;
    }

    public BackendResponse refresh(String refreshToken) {
        BackendResponse universeResponse=initializeResponse();
            if (jwtProvider.validateRefreshToken(refreshToken)) {
                final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
                final String login = claims.getSubject();
                User user = userService.findByLogin(login);
                final String accessToken = jwtProvider.generateAccessTokenForLogin(user);
                final String newRefreshToken = jwtProvider.generateRefreshToken(user);
                HashMap<String, String> map = new HashMap<>();
                universeResponse.setMessage("Токен обновлен");
                universeResponse.addMap(map);
                universeResponse.addData(map, "accessToken", accessToken);
                universeResponse.addData(map, "refreshToken", newRefreshToken);

                return universeResponse;
            }
        throw new AuthException("Невалидный JWT токен");
    }

}
