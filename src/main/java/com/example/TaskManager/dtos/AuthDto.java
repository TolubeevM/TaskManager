package com.example.TaskManager.dtos;

import com.example.TaskManager.models.User;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;


@Data
//@Tag(name = "DTOs", description = "Data transfer objects")
@Schema(description = "DTO для аутентификации пользователя")
public class AuthDto {

    @Schema(description = "Идентификатор пользователя", example = "1")
    private Long uid;

    @Schema(description = "Электронная почта пользователя", example = "user@example.com")
    private String email;

    @Schema(description = "Имя пользователя", example = "JohnDoe")
    private String username;

    @Schema(description = "Роль пользователя", example = "ROLE_ADMIN")
    private String role;

    @Schema(description = "JWT токен пользователя")
    private String token;

    public AuthDto(User user, String token) {
        this.uid = user.getId();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.role = user.getRoles().toString().replace("[", "").replace("]", "");
        this.token = token;
    }
}