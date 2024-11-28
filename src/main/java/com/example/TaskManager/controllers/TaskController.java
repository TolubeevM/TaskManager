package com.example.TaskManager.controllers;

import com.example.TaskManager.dtos.TaskDto;
import com.example.TaskManager.dtos.TaskShowDto;
import com.example.TaskManager.models.CustomUserDetails;
import com.example.TaskManager.models.enums.Priority;
import com.example.TaskManager.models.enums.Status;
import com.example.TaskManager.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/task")
@Tag(name = "Tasks", description = "Операции с задачами")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @Operation(summary = "Создать задачу", description = "Создает новую задачу и возвращает её (доступно только администратору)")
    @PostMapping("/createTask")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createTask(
            @RequestBody @Parameter(description = "Данные для создания задачи") TaskDto taskDto,
            @AuthenticationPrincipal @Parameter(hidden = true) CustomUserDetails userDetails) {
        try {
            return ResponseEntity.ok(taskService.createTask(taskDto, userDetails));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Добавить исполнителя к задаче", description = "Добавляет исполнителя к задаче по ID (доступно только администратору)")
    @PutMapping("/addExecutors")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addExecutors(
            @RequestParam @Parameter(description = "ID задачи") Long taskId,
            @RequestParam @Parameter(description = "ID пользователя-исполнителя") Long userId) {
        try {
            taskService.changeExecutor(taskId, userId);
            return ResponseEntity.ok("Executor added");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Удалить исполнителя из задачи", description = "Удаляет исполнителя из задачи по ID (доступно только администратору)")
    @DeleteMapping("/deleteExecutors")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteExecutors(
            @RequestParam @Parameter(description = "ID задачи") Long taskId,
            @RequestParam @Parameter(description = "ID пользователя-исполнителя") Long userId) {
        try {
            taskService.deleteExecutor(taskId, userId);
            return ResponseEntity.ok("Executor deleted");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Изменить приоритеты задачи", description = "Изменяет приоритеты для задачи по ID (доступно только администратору)")
    @PutMapping("/{taskId}/changePriority")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> changePriority(
            @PathVariable @Parameter(description = "ID задачи") Long taskId,
            @RequestBody @Parameter(description = "Новый список приоритетов") Set<Priority> priorities,
            @AuthenticationPrincipal @Parameter(hidden = true) CustomUserDetails userDetails) {
        taskService.changePriority(taskId, priorities, userDetails);
        return ResponseEntity.ok("Priority changed");
    }

    @Operation(summary = "Изменить статус задачи", description = "Изменяет статус задачи по ID")
    @PutMapping("/{taskId}/changeStatus")
    public ResponseEntity<?> changeStatus(
            @PathVariable @Parameter(description = "ID задачи") Long taskId,
            @RequestBody @Parameter(description = "Новый список статусов") Set<Status> statuses,
            @AuthenticationPrincipal @Parameter(hidden = true) CustomUserDetails userDetails) {
        taskService.changeStatus(taskId, statuses, userDetails);
        return ResponseEntity.ok("Status changed");
    }

    @Operation(summary = "Изменить текст задачи", description = "Изменяет текст задачи по ID (доступно только администратору)")
    @PutMapping("/changeText")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> changeText(
            @RequestParam @Parameter(description = "ID задачи") Long taskId,
            @RequestParam @Parameter(description = "Новый текст задачи") String text) {
        taskService.changeText(taskId, text);
        return ResponseEntity.ok("Text changed");
    }

    @Operation(summary = "Получить все задачи", description = "Возвращает все задачи в виде списка DTO")
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @Operation(summary = "Получить задачи по автору или исполнителю",
            description = "Фильтрует задачи по автору или исполнителю с поддержкой пагинации")
    @GetMapping("/getAuthorsOrExecutors")
    public ResponseEntity<Page<TaskShowDto>> getTasks(
            @RequestParam(required = false) @Parameter(description = "Имя автора задачи") String author,
            @RequestParam(required = false) @Parameter(description = "Имя исполнителя задачи") String executor,
            @RequestParam(defaultValue = "0") @Parameter(description = "Номер страницы (для пагинации)") int page,
            @RequestParam(defaultValue = "10") @Parameter(description = "Размер страницы (для пагинации)") int size) {
        return ResponseEntity.ok(taskService.getTasksByAuthorOrExecutor(author, executor, page, size));
    }
}
