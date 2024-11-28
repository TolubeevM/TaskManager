package com.example.TaskManager.dtos;

import com.example.TaskManager.models.enums.Priority;
import com.example.TaskManager.models.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;

import java.util.Set;

@Data
//@Tag(name = "DTOs", description = "Data transfer objects")
@Schema(description = "DTO для создания или обновления задачи")
public class TaskDto {

    @Schema(description = "Приоритеты задачи", example = "[PRIORITY_HIGH]")
    private Set<Priority> priorities;

    @Schema(description = "Статусы задачи", example = "[STATUS_INPROGRESS]")
    private Set<Status> statuses;

    @Schema(description = "Текст задачи", example = "Complete the project documentation")
    private String text;

    @Schema(description = "Список ID исполнителей задачи", example = "[1, 2, 3]")
    private Set<Long> executorIds;
}
