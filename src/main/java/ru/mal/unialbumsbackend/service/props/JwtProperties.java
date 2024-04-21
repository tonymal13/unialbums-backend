package ru.mal.unialbumsbackend.service.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class JwtProperties {

    @Value("${jwt.secret.access}") String jwtAccessSecret;
    @Value("${jwt.secret.refresh}") String jwtRefreshSecret;

}
