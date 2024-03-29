package ru.mal.unialbumsbackend.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LogInResponse {

    private final String type = "Bearer";
    private String accessToken;
    private String login;
    private String firstName;
    private String lastName;

}
