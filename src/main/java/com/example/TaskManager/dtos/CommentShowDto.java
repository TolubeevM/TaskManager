package com.example.TaskManager.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;

@Data
@Tag(name = "DTOs", description = "Data transfer objects")
@Schema(description = "DTO для отображения комментариев")
public class CommentShowDto {

    @Schema(description = "ID комментария", example = "1")
    private Long id;

    @Schema(description = "Имя пользователя, оставившего комментарий", example = "Max")
    private String username;

    @Schema(description = "Текст комментария", example = "This is a sample comment")
    private String text;
}

