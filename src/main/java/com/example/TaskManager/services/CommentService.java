package com.example.TaskManager.services;

import com.example.TaskManager.dtos.CommentShowDto;
import com.example.TaskManager.models.Comment;
import com.example.TaskManager.models.CustomUserDetails;
import com.example.TaskManager.models.Task;
import com.example.TaskManager.models.User;
import com.example.TaskManager.repositories.CommentRepository;
import com.example.TaskManager.repositories.TaskRepository;
import com.example.TaskManager.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public void addCommentToTask(Long taskId, String commentText, CustomUserDetails userDetails) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with ID: " + taskId));

        User user = userRepository.findByEmail(userDetails.getEmail());
        boolean isAdmin = user.getRoles().stream()
                .anyMatch(role -> role.name().equals("ROLE_ADMIN"));
        boolean isExecutor = task.getExecutors().contains(user);
        if (isAdmin || isExecutor) {
            Comment comment = new Comment();
            comment.setTask(task);
            comment.setUser(user);
            comment.setText(commentText);

            task.getComments().add(comment);
            commentRepository.save(comment);
        } else
            throw new SecurityException("Access denied: You are neither an execubootor of this task nor an administrator.");
    }

    public List<CommentShowDto> getAll(){
        List<CommentShowDto> comments = new ArrayList<>(Collections.emptyList());
        List<Comment> rawComments = commentRepository.findAll();
        for (Comment comment : rawComments) {
            comments.add(convertCommentToDto(comment));
        }
        return comments;
    }

    public CommentShowDto convertCommentToDto(Comment comment) {
        CommentShowDto dto = new CommentShowDto();
        dto.setId(comment.getId());
        dto.setText(comment.getText());
        dto.setUsername(comment.getUser().getUsername());
        return dto;
    }
}
