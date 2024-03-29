package ru.mal.unialbumsbackend.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
@Getter
@AllArgsConstructor
public class AccessTokenResponse {

    private final String type = "Bearer";
    private String accessToken;
}