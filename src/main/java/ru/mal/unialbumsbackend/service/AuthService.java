package ru.mal.unialbumsbackend.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.mal.unialbumsbackend.exception.AuthException;
import ru.mal.unialbumsbackend.web.dto.auth.RefreshAndAccessDto;
import ru.mal.unialbumsbackend.web.security.JwtProvider;
import ru.mal.unialbumsbackend.domain.*;
import ru.mal.unialbumsbackend.web.dto.auth.LogInDto;
import ru.mal.unialbumsbackend.exception.UserNotFoundException;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtProvider jwtProvider;

    private final PasswordEncoder passwordEncoder;

    public RefreshAndAccessDto login(LogInDto logInDto) {
        RefreshAndAccessDto refreshAndAccessDto=new RefreshAndAccessDto();
        User user = userService.findByUsername(logInDto.getUsername());
            if (passwordEncoder.matches(logInDto.getPassword() ,user.getPassword())){
                final String accessToken = jwtProvider.generateAccessTokenForLogin(user);
                final String refreshToken = jwtProvider.generateRefreshToken(user);
                HashMap<String,String> map=new HashMap<>();
                refreshAndAccessDto.setAccessToken(accessToken);
                refreshAndAccessDto.setRefreshToken(refreshToken);

            } else {
                throw new UserNotFoundException("Пользователь не найден");
            }
        return refreshAndAccessDto;
    }

    public RefreshAndAccessDto getAccessToken(String refreshToken) {
        RefreshAndAccessDto tokens=new RefreshAndAccessDto();
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            User user=userService.findByUsername(login);
            String accessToken = jwtProvider.generateAccessTokenForLogin(user);
            tokens.setAccessToken(accessToken);
        }
        return tokens;
    }

    public RefreshAndAccessDto refresh(String refreshToken) {
        RefreshAndAccessDto tokens=new RefreshAndAccessDto();
            if (jwtProvider.validateRefreshToken(refreshToken)) {
                final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
                final String login = claims.getSubject();
                User user = userService.findByUsername(login);
                final String accessToken = jwtProvider.generateAccessTokenForLogin(user);
                final String newRefreshToken = jwtProvider.generateRefreshToken(user);
                tokens.setRefreshToken(newRefreshToken);
                tokens.setAccessToken(accessToken);

                return tokens;
            }
        throw new AuthException("Невалидный JWT токен");
    }

    public String generateRefreshToken(User user) {
        return jwtProvider.generateRefreshToken(user);
    }
}
