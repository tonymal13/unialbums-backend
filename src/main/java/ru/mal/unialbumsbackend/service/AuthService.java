package ru.mal.unialbumsbackend.service;

import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.mal.unialbumsbackend.web.security.JwtProvider;
import ru.mal.unialbumsbackend.domain.*;

import ru.mal.unialbumsbackend.web.dto.auth.LogInRequest;
import ru.mal.unialbumsbackend.web.dto.UniverseResponse;
import ru.mal.unialbumsbackend.exception.AuthException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final Map<String, String> refreshStorage = new HashMap<>();
    private final JwtProvider jwtProvider;

    private final PasswordEncoder passwordEncoder;

    public UniverseResponse login(@NonNull LogInRequest authRequest) {
        Optional<User> user = userService.findByLogin(authRequest.getUsername());
        UniverseResponse universeResponse=new UniverseResponse();
        universeResponse.setData(new ArrayList<>());
        if(user.isPresent()) {
            if (passwordEncoder.matches(authRequest.getPassword() ,user.get().getPassword())){
                final String accessToken = jwtProvider.generateAccessTokenForLogin(user.get());
                final String refreshToken = jwtProvider.generateRefreshToken(user.get());
                refreshStorage.put(user.get().getUsername(), refreshToken);
                HashMap<String,String> map=new HashMap<>();
                universeResponse.addMap(map);
                universeResponse.addData(map,"accessToken",accessToken);
                universeResponse.addData(map,"refreshToken",refreshToken);
                universeResponse.setMessage("Вы зашли в аккаунт");
            } else {
                universeResponse.setMessage("Неправильный пароль");
                throw new AuthException("Неправильный пароль");
            }
        }

        else {
            universeResponse.setMessage("Пользователь не найден");
            throw new AuthException("Пользователь не найден");
        }
        return universeResponse;
    }

    public UniverseResponse getAccessToken(@NonNull String refreshToken) {
        UniverseResponse universeResponse=new UniverseResponse();
        universeResponse.setData(new ArrayList<>());
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                Optional<User> user=userService.findByLogin(login);
                if(user.isEmpty()) {
                    universeResponse.setMessage("Пользователь не найден");
                    throw new AuthException("Пользователь не найден");
                }
                else {
                    String accessToken = jwtProvider.generateAccessTokenForLogin(user.get());
                    HashMap<String,String> map=new HashMap<>();
                    universeResponse.addData(map,"accessToken", accessToken);
                }

            }
        }
        return universeResponse;
    }

    public UniverseResponse refresh(@NonNull String refreshToken) {
        UniverseResponse universeResponse=new UniverseResponse();
        universeResponse.setData(new ArrayList<>());
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                Optional<User> user=userService.findByLogin(login);
                if(user.isEmpty()) {
                    universeResponse.setMessage("Пользователь не найден");
                    throw new AuthException("Пользователь не найден");
                }
                final String accessToken = jwtProvider.generateAccessTokenForLogin(user.get());
                final String newRefreshToken = jwtProvider.generateRefreshToken(user.get());
                refreshStorage.put(user.get().getUsername(), newRefreshToken);
                HashMap<String,String> map=new HashMap<>();
                universeResponse.setMessage("Токен обновлен");
                universeResponse.addMap(map);
                universeResponse.addData(map,"accessToken", accessToken);
                universeResponse.addData(map,"refreshToken",newRefreshToken);

                return universeResponse;
            }
        }
        universeResponse.setMessage("Невалидный JWT токен");
        throw new AuthException("Невалидный JWT токен");
    }

}
