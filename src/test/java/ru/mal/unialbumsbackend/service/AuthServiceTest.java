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
import ru.mal.unialbumsbackend.web.dto.UniverseResponse;
import ru.mal.unialbumsbackend.web.dto.auth.LogInDto;
import ru.mal.unialbumsbackend.web.security.JwtProvider;

import java.util.*;

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
        LogInDto request = new LogInDto();
        request.setUsername(username);
        request.setPassword( password);
        User user = new User();
        user.setUsername(username);

        user.setPassword(bCryptPasswordEncoder.encode(password));

        Mockito.when( bCryptPasswordEncoder.matches(password,bCryptPasswordEncoder.encode(password)))
                .thenReturn(true);

        Mockito.when(userService.findByLogin(username))
                .thenReturn(Optional.of(user));

        Mockito.when(jwtProviderMock.generateAccessTokenForLogin(user))
                .thenReturn(accessToken);
        Mockito.when(jwtProviderMock.generateRefreshToken(user))
                .thenReturn(refreshToken);

        UniverseResponse response = authService.login(request);

        Assertions.assertEquals(response.getData().get(0).get("accessToken"), accessToken);
        Assertions.assertEquals(response.getData().get(0).get("refreshToken"), refreshToken);
    }

    @Test
    void loginWithIncorrectUsername() {
        String username = "a";
        String password = "a";
        LogInDto request = new LogInDto();
        request.setUsername(username);
        request.setPassword(password);
        User user = new User();
        user.setUsername(username);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        Mockito.when(userService.findByLogin(username))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class,
                () -> authService.login(request));
    }

    @Test
    void loginWithIncorrectPassword() {
        String username = "a";
        String password = "a";
        LogInDto request = new LogInDto();
        request.setUsername(username);
        request.setPassword(password);
        User user = new User();
        user.setUsername(username);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        Mockito.when( bCryptPasswordEncoder.matches(password,bCryptPasswordEncoder.encode(password)))
                        .thenReturn(false);
        Mockito.when(userService.findByLogin(username))
                .thenReturn(Optional.of(user));

        Assertions.assertThrows(UserNotFoundException.class,
                () -> authService.login(request));
    }

//    @Test
//    void refresh() {
//                String login = "a";
//        String password = "a";
//        LogInRequest request = new LogInRequest();
//        request.setLogin(login);
//        request.setPassword( password);
//        User user = new User();
//        user.setLogin(login);
//        user.setPassword(password);
//        String refreshToken = jwtProvider.generateRefreshToken(user);
//        System.out.println(refreshToken);
//        BackendResponse response = new BackendResponse();
//        response.setData(new ArrayList<>());
//        HashMap<String,String> map= new HashMap<>();
//        response.addMap(map);
//        Mockito.when(userService.findByLogin(login))
//                .thenReturn(Optional.of(user));
////        response.addData(map,"accessToken",accessToken);
//        response.addData(map,"refreshToken",refreshToken);
////        Mockito.when(jwtProvider.validateRefreshToken(refreshToken))
////                .thenReturn(true);
//        BackendResponse testResponse = authService.refresh(refreshToken);
////        Mockito.verify(jwtProviderMock).generateRefreshToken(user);
//        Assertions.assertEquals(testResponse, response);
//    }

}
