package ru.mal.unialbumsbackend.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TokensResponse {

    private final String type = "Bearer";
    private String accessToken;
    private String refreshToken;

}
