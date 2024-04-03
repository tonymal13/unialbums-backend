package ru.mal.unialbumsbackend.service;

import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.mal.unialbumsbackend.domain.*;

import ru.mal.unialbumsbackend.domain.requests.LogInRequest;
import ru.mal.unialbumsbackend.domain.response.TokensResponse;
import ru.mal.unialbumsbackend.domain.response.UniverseResponse;
import ru.mal.unialbumsbackend.exception.AuthException;
import ru.mal.unialbumsbackend.util.UserNotFoundException;

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
        Optional<User> user = userService.getByLogin(authRequest.getLogin());
        UniverseResponse universeResponse=new UniverseResponse();
        universeResponse.setData(new ArrayList<>());
        if(user.isPresent()) {
            if (passwordEncoder.matches(authRequest.getPassword() ,user.get().getPassword())){
                final String accessToken = jwtProvider.generateAccessTokenForLogin(user.get());
                final String refreshToken = jwtProvider.generateRefreshToken(user.get());
                refreshStorage.put(user.get().getLogin(), refreshToken);
                HashMap<String,String> map=new HashMap<>();
                universeResponse.addMap(map);
                universeResponse.addData(map,"accessToken",accessToken);
                universeResponse.addData(map,"refreshToken",refreshToken);
                universeResponse.setMessage("Logged in");
            } else {
                universeResponse.setMessage("Wrong password");
                throw new AuthException("Wrong password");
            }
        }

        else {
            universeResponse.setMessage("User is not found");
            throw new UserNotFoundException();
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
//                final User user = userService.getByLogin(login)
//                        .orElseThrow(() -> new AuthException("Пользователь не найден"));
                Optional<User> user=userService.getByLogin(login);
                if(user.isEmpty()) {
                    universeResponse.setMessage("User not found");
                    throw new AuthException("User not found");
                }
                else {
                    String accessToken = jwtProvider.generateAccessToken(user.get());
                    HashMap<String,String> map=new HashMap<>();
//                    universeResponse.addMap(map);
                    universeResponse.addData(map,"accessToken", accessToken);
                }

            }
        }
        return universeResponse;
    }

    public TokensResponse refresh(@NonNull String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = userService.getByLogin(login)
                        .orElseThrow(() -> new AuthException("Пользователь не найден"));
                final String accessToken = jwtProvider.generateAccessToken(user);
                final String newRefreshToken = jwtProvider.generateRefreshToken(user);
                refreshStorage.put(user.getLogin(), newRefreshToken);
                return new TokensResponse(accessToken, newRefreshToken);
            }
        }
        throw new AuthException("Невалидный JWT токен");
    }


}
