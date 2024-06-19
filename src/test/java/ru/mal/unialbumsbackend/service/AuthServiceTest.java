package ru.mal.unialbumsbackend.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.mal.unialbumsbackend.config.TestConfig;
import ru.mal.unialbumsbackend.domain.User;

import ru.mal.unialbumsbackend.exception.UserNotFoundException;
import ru.mal.unialbumsbackend.repositories.AlbumRepository;
import ru.mal.unialbumsbackend.repositories.UserRepository;
import ru.mal.unialbumsbackend.web.dto.BackendResponse;
import ru.mal.unialbumsbackend.web.dto.auth.LogInDto;
import ru.mal.unialbumsbackend.web.dto.auth.RefreshAndAccessDto;
import ru.mal.unialbumsbackend.web.security.JwtProvider;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Import(TestConfig.class)
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AlbumRepository albumRepository;

    @MockBean
    private JwtProvider jwtProviderMock;

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtProvider jwtProvider;

    @Test
    void login() {
        String username = "a";
        String password = "a";
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";
        LogInDto logInDto = new LogInDto();
        logInDto.setUsername(username);
        logInDto.setPassword( password);
        User user = new User();
        user.setUsername(username);

        user.setPassword(bCryptPasswordEncoder.encode(password));

        Mockito.when( bCryptPasswordEncoder.matches(password,bCryptPasswordEncoder.encode(password)))
                .thenReturn(true);

        Mockito.when(userService.findByUsername(username))
                .thenReturn(user);

        Mockito.when(jwtProviderMock.generateAccessTokenForLogin(user))
                .thenReturn(accessToken);
        Mockito.when(jwtProviderMock.generateRefreshToken(user))
                .thenReturn(refreshToken);

        RefreshAndAccessDto tokens = authService.login(logInDto);

        Assertions.assertEquals(tokens.getAccessToken(), accessToken);
        Assertions.assertEquals(tokens.getRefreshToken(), refreshToken);
    }

    @Test
    void loginWithIncorrectUsername() {
        String username = "a";
        String password = "a";
        LogInDto logInDto = new LogInDto();
        logInDto.setUsername(username);
        logInDto.setPassword(password);
        User user = new User();
        user.setUsername(username);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        Mockito.when(userService.findByUsername(username))
                .thenThrow(UserNotFoundException.class);

        Assertions.assertThrows(UserNotFoundException.class,
                () -> authService.login(logInDto));
    }

    @Test
    void loginWithIncorrectPassword() {
        String username = "a";
        String password = "a";
        LogInDto logInDto = new LogInDto();
        logInDto.setUsername(username);
        logInDto.setPassword(password);
        User user = new User();
        user.setUsername(username);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        Mockito.when( bCryptPasswordEncoder.matches(password,bCryptPasswordEncoder.encode(password)))
                        .thenReturn(false);
        Mockito.when(userService.findByUsername(username))
                .thenReturn(user);

        Assertions.assertThrows(UserNotFoundException.class,
                () -> authService.login(logInDto));
    }

}
