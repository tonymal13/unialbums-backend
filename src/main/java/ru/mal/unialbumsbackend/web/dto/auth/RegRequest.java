package ru.mal.unialbumsbackend.web.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegRequest {

    private String firstName;


    private String lastName;


    private String login;

    private String password;
}
