package ru.mal.unialbumsbackend.web.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LogInRequest {

    private String login;
    private String password;

}
