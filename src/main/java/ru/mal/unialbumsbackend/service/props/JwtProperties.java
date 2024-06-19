package ru.mal.unialbumsbackend.service.props;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class JwtProperties {

    @Value("${jwt.secret.access}") String jwtAccessSecret;
    @Value("${jwt.secret.refresh}") String jwtRefreshSecret;

}
