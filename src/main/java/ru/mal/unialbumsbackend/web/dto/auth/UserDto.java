package ru.mal.unialbumsbackend.web.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "User Dto")
public class UserDto {

    @Schema(description = "firstname",example = "Иван")
    private String firstName;

    @Schema(description = "lastname",example = "Иванов")
    private String lastName;

    @Schema(description="username",example = "ivan123")
    private String username;

    @Schema(description = "password",example = "123")
    private String password;

    public UserDto(String firstName, String lastName, String username) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
    }

    public UserDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
