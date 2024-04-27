package ru.mal.unialbumsbackend.web.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshJwtDto {

    @Schema(description="refreshToken",example = "refreshToken")
    public String refreshToken;

}
