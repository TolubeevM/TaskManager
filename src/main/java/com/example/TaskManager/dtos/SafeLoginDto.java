package com.example.TaskManager.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;

@Data
//@Tag(name = "DTOs", description = "Data transfer objects")
@Schema(description = "DTO для безопасного входа пользователя")
public class SafeLoginDto {

    @Schema(description = "Электронная почта пользователя", example = "user@example.com")
    private String email;

    @Schema(description = "Пароль пользователя", example = "securePassword123")
    private String password;
}
