package ru.mal.unialbumsbackend.service;

import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.mal.unialbumsbackend.domain.User;
import ru.mal.unialbumsbackend.web.dto.BackendResponse;
import ru.mal.unialbumsbackend.web.security.JwtProvider;

import ru.mal.unialbumsbackend.web.dto.auth.LogInRequest;
import ru.mal.unialbumsbackend.web.exception.AuthException;

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

    public BackendResponse login(@NonNull LogInRequest authRequest) {
        Optional<User> user = userService.findByLogin(authRequest.getLogin());
        BackendResponse backendResponse =new BackendResponse();
        backendResponse.setData(new ArrayList<>());
        if(user.isPresent()) {
            if (passwordEncoder.matches(authRequest.getPassword() ,user.get().getPassword())){
                final String accessToken = jwtProvider.generateAccessTokenForLogin(user.get());
                final String refreshToken = jwtProvider.generateRefreshToken(user.get());
                refreshStorage.put(user.get().getLogin(), refreshToken);
                HashMap<String,String> map=new HashMap<>();
                backendResponse.addMap(map);
                backendResponse.addData(map,"accessToken",accessToken);
                backendResponse.addData(map,"refreshToken",refreshToken);
                backendResponse.setMessage("Вы зашли в аккаунт");
            } else {
                backendResponse.setMessage("Неправильный пароль");
                throw new AuthException("Неправильный пароль");
            }
        }

        else {
            backendResponse.setMessage("Пользователь не найден");
            throw new AuthException("Пользователь не найден");
        }
        return backendResponse;
    }

    public BackendResponse getAccessToken(@NonNull String refreshToken) {
        BackendResponse backendResponse =new BackendResponse();
        backendResponse.setData(new ArrayList<>());
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                Optional<User> user=userService.findByLogin(login);
                if(user.isEmpty()) {
                    backendResponse.setMessage("Пользователь не найден");
                    throw new AuthException("Пользователь не найден");
                }
                else {
                    String accessToken = jwtProvider.generateAccessTokenForLogin(user.get());
                    HashMap<String,String> map=new HashMap<>();
                    backendResponse.addData(map,"accessToken", accessToken);
                }

            }
        }
        return backendResponse;
    }

    public BackendResponse refresh(@NonNull String refreshToken) {
        BackendResponse backendResponse =new BackendResponse();
        backendResponse.setData(new ArrayList<>());
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                Optional<User> user=userService.findByLogin(login);
                if(user.isEmpty()) {
                    backendResponse.setMessage("Пользователь не найден");
                    throw new AuthException("Пользователь не найден");
                }
                final String accessToken = jwtProvider.generateAccessTokenForLogin(user.get());
                final String newRefreshToken = jwtProvider.generateRefreshToken(user.get());
                refreshStorage.put(user.get().getLogin(), newRefreshToken);
                HashMap<String,String> map=new HashMap<>();
                backendResponse.setMessage("Токен обновлен");
                backendResponse.addMap(map);
                backendResponse.addData(map,"accessToken", accessToken);
                backendResponse.addData(map,"refreshToken",newRefreshToken);

                return backendResponse;
            }
        }
        backendResponse.setMessage("Невалидный JWT токен");
        throw new AuthException("Невалидный JWT токен");
    }

}
