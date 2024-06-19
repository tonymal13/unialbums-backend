package ru.mal.unialbumsbackend.web.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LogInDto {

    @Schema(description="username",example = "ivan123")
    private String username;
    @Schema(description = "password",example = "123")
    private String password;

}
