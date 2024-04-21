package ru.mal.unialbumsbackend.web.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtRequest {

    public String refreshToken;

}
