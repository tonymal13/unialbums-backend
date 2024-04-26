package ru.mal.unialbumsbackend.web.dto.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegRequest {

    private String firstName;


    private String lastName;


    private String username;

    private String password;

    public RegRequest(String firstName, String lastName, String username) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
    }
}
