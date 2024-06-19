package ru.mal.unialbumsbackend.web.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RefreshAndAccessDto {

    @Schema(description="refreshToken",example = "refreshToken")
    private String refreshToken;

    @Schema(description="accessToken",example = "accessToken")
    private String accessToken;

}
