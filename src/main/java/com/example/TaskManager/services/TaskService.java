package com.example.TaskManager.services;

import com.example.TaskManager.dtos.TaskDto;
import com.example.TaskManager.dtos.TaskShowDto;
import com.example.TaskManager.dtos.CommentShowDto;
import com.example.TaskManager.models.Comment;
import com.example.TaskManager.models.CustomUserDetails;
import com.example.TaskManager.models.Task;
import com.example.TaskManager.models.User;
import com.example.TaskManager.models.enums.Priority;
import com.example.TaskManager.models.enums.Status;
import com.example.TaskManager.repositories.TaskRepository;
import com.example.TaskManager.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLDataException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CommentService commentService;

    public Task createTask(TaskDto taskDto, CustomUserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getEmail());
        Task task = new Task();
        task.setText(taskDto.getText());
        task.setPriorities(taskDto.getPriorities());
        task.setStatuses(taskDto.getStatuses());
        task.setAuthor(user.getUsername());

        Set<User> executors = taskDto.getExecutorIds().stream()
                .map(id -> userRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id)))
                .collect(Collectors.toSet());

        task.setExecutors(executors);
        return taskRepository.save(task);
    }

    @Transactional
    public void changeExecutor(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with ID: " + taskId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        if (task.getExecutors().contains(user)) throw new IllegalArgumentException("User already executor");
        else {
            task.getExecutors().add(user);
            taskRepository.save(task);
        }
    }

    @Transactional
    public void deleteExecutor(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with ID: " + taskId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        task.getExecutors().remove(user);

        taskRepository.save(task);
    }

    @Transactional
    public void changePriority(Long taskId, Set<Priority> newPriorities, CustomUserDetails userDetails) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with ID: " + taskId));
        task.setPriorities(newPriorities);
        taskRepository.save(task);
    }

    @Transactional
    public void changeStatus(Long taskId, Set<Status> newStatuses, CustomUserDetails userDetails) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with ID: " + taskId));
        User currentUser = userRepository.findByEmail(userDetails.getEmail());

        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(role -> role.name().equals("ROLE_ADMIN"));

        boolean isExecutor = task.getExecutors().contains(currentUser);
        if (isAdmin || isExecutor) {
            task.setStatuses(newStatuses);
            taskRepository.save(task);
        } else
            throw new SecurityException("Access denied: You are neither an execubootor of this task nor an administrator.");
    }

    public void changeText(Long taskId, String text) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with ID: " + taskId));
        task.setText(text);
        taskRepository.save(task);
    }

    public List<TaskShowDto> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();

        return tasks.stream().map(task -> {
            TaskShowDto taskShowDto = new TaskShowDto();
            taskShowDto.setAuthor(task.getAuthor());
            taskShowDto.setStatuses(task.getStatuses());
            taskShowDto.setPriorities(task.getPriorities());
            taskShowDto.setExecutors(task.getExecutors().stream()
                    .map(User::getUsername)
                    .collect(Collectors.toList()));
            List<CommentShowDto> comments = new ArrayList<>(Collections.emptyList());
            List<Comment> rawComments = task.getComments();
            for (Comment comment : rawComments) {
                comments.add(commentService.convertCommentToDto(comment));
            }
            taskShowDto.setComments(comments);
            return taskShowDto;
        }).collect(Collectors.toList());
    }

    public Page<TaskShowDto> getTasksByAuthorOrExecutor(String author, String executorUsername, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Task> tasks = taskRepository.findByAuthorOrExecutor(author, executorUsername, pageable);

        return tasks.map(task -> {
            TaskShowDto taskShowDto = new TaskShowDto();
            taskShowDto.setAuthor(task.getAuthor());
            taskShowDto.setStatuses(task.getStatuses());
            taskShowDto.setPriorities(task.getPriorities());
            taskShowDto.setExecutors(task.getExecutors().stream()
                    .map(User::getUsername)
                    .collect(Collectors.toList()));
            List<CommentShowDto> comments = task.getComments().stream()
                    .map(commentService::convertCommentToDto)
                    .collect(Collectors.toList());
            taskShowDto.setComments(comments);
            return taskShowDto;
        });
    }


}
