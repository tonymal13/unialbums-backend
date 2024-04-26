package ru.mal.unialbumsbackend.web.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegRequest {

    private String firstName;


    private String lastName;


    private String username;

    private String password;
}
