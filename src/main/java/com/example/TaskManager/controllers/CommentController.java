package com.example.TaskManager.controllers;

import com.example.TaskManager.models.CustomUserDetails;
import com.example.TaskManager.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/comment")
@Tag(name = "Comments", description = "Операции с комментариями")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "Добавить комментарий", description = "Добавляет комментарий к задаче по ID задачи")
    @PostMapping("/{taskId}/add")
    public ResponseEntity<?> addComment(
            @PathVariable @Parameter(description = "ID задачи") Long taskId,
            @RequestParam @Parameter(description = "Текст комментария") String text,
            @AuthenticationPrincipal @Parameter(hidden = true) CustomUserDetails userDetails) {
        commentService.addCommentToTask(taskId, text, userDetails);
        return ResponseEntity.ok("Comment added");
    }

    @Operation(summary = "Получить все комментарии", description = "Возвращает список всех комментариев (доступно только администратору)")
    @GetMapping("/getAll")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAll(
            @AuthenticationPrincipal @Parameter(hidden = true) CustomUserDetails userDetails) {
        return ResponseEntity.ok(commentService.getAll());
    }
}

