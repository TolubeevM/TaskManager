package com.example.TaskManager.dtos;

import com.example.TaskManager.models.enums.Priority;
import com.example.TaskManager.models.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
//@Tag(name = "DTOs", description = "Data transfer objects")
@Schema(description = "DTO для отображения информации о задаче")
public class TaskShowDto {

    @Schema(description = "Автор задачи", example = "Max")
    private String author;

    @Schema(description = "Статусы задачи", example = "[STATUS_INPROGRESS]")
    private Set<Status> statuses;

    @Schema(description = "Приоритеты задачи",  example = "[PRIORITY_HIGH]")
    private Set<Priority> priorities;

    @Schema(description = "Список исполнителей задачи", example = "[\"executor1\", \"executor2\"]")
    private List<String> executors;

    @Schema(description = "Список комментариев к задаче")
    private List<CommentShowDto> comments;
}
