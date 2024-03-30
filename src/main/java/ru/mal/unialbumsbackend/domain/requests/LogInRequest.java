package ru.mal.unialbumsbackend.domain.requests;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LogInRequest {

    private String login;
    private String password;

}
