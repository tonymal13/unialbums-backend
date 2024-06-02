package ru.mal.unialbumsbackend.config;//package ru.mal.unialbumsbackend.config;

import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import ru.mal.unialbumsbackend.repositories.AlbumRepository;
import ru.mal.unialbumsbackend.repositories.UserRepository;
import ru.mal.unialbumsbackend.service.AuthService;
import ru.mal.unialbumsbackend.service.ImageService;
import ru.mal.unialbumsbackend.service.UserService;
import ru.mal.unialbumsbackend.service.props.MinioProperties;
import ru.mal.unialbumsbackend.web.security.JwtAuthentication;
import ru.mal.unialbumsbackend.web.security.JwtProvider;
import ru.mal.unialbumsbackend.service.props.JwtProperties;

@TestConfiguration
@RequiredArgsConstructor
public class TestConfig {

    @Bean
    @Primary
    public BCryptPasswordEncoder testPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtProperties jwtProperties() {
        return new JwtProperties();
    }

    @Bean
    public JwtAuthentication jwtAuthentication(
    ) {
        return new JwtAuthentication();
    }

    @Bean
    public MinioClient minioClient() {
        return Mockito.mock(MinioClient.class);
    }

    @Bean
    public MinioProperties minioProperties() {
        return new MinioProperties();
    }

    @Bean
    @Primary
    public ImageService imageService() {
        return new ImageService(minioProperties(),minioClient());
    }

    @Bean
    public JwtProvider jwtProvider() {
        return new JwtProvider(jwtProperties());
    }

    @Bean
    @Primary
    public UserService userService(
            final UserRepository userRepository) {
        return new UserService(
                userRepository,
                testPasswordEncoder(),imageService());
    }

    @Bean
    @Primary
    public AuthService authService(
            final UserRepository userRepository
    ) {
        return new AuthService(
                userService(userRepository),
                jwtProvider(),
                testPasswordEncoder()
        );
    }

    @Bean
    public UserRepository userRepository() {
        return Mockito.mock(UserRepository.class);
    }

    @Bean
    public AlbumRepository taskRepository() {
        return Mockito.mock(AlbumRepository.class);
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return Mockito.mock(AuthenticationManager.class);
    }


}
