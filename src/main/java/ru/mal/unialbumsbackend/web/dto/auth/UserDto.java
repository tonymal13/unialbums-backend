package ru.mal.unialbumsbackend.web.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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

}
