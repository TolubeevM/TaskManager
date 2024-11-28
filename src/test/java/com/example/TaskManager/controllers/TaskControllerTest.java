package com.example.TaskManager.controllers;

import com.example.TaskManager.dtos.TaskDto;
import com.example.TaskManager.dtos.TaskShowDto;
import com.example.TaskManager.models.CustomUserDetails;
import com.example.TaskManager.repositories.UserRepository;
import com.example.TaskManager.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TaskControllerTest {

    @InjectMocks
    private TaskController taskController;

    @Mock
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void createTask_Failure() {
        TaskDto taskDto = new TaskDto();
        CustomUserDetails userDetails = new CustomUserDetails(
                "username", "password", "email", 1L, List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );
        when(taskService.createTask(taskDto, userDetails)).thenThrow(new IllegalArgumentException("Error"));

        ResponseEntity<?> response = taskController.createTask(taskDto, userDetails);

        assertEquals(ResponseEntity.badRequest().body("Error"), response);
    }

    @Test
    void addExecutors_Success() {
        Long taskId = 1L;
        Long userId = 2L;

        ResponseEntity<?> response = taskController.addExecutors(taskId, userId);

        assertEquals(ResponseEntity.ok("Executor added"), response);
        verify(taskService, times(1)).changeExecutor(taskId, userId);
    }

    @Test
    void addExecutors_Failure() {
        Long taskId = 1L;
        Long userId = 2L;

        doThrow(new IllegalArgumentException("Error")).when(taskService).changeExecutor(taskId, userId);

        ResponseEntity<?> response = taskController.addExecutors(taskId, userId);

        assertEquals(ResponseEntity.badRequest().body("Error"), response);
    }

    @Test
    void deleteExecutors_Success() {
        Long taskId = 1L;
        Long userId = 2L;

        ResponseEntity<?> response = taskController.deleteExecutors(taskId, userId);

        assertEquals(ResponseEntity.ok("Executor deleted"), response);
        verify(taskService, times(1)).deleteExecutor(taskId, userId);
    }


    @Test
    void getAllTasks() {
        when(taskService.getAllTasks()).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = taskController.getAllTasks();

        assertEquals(ResponseEntity.ok(Collections.emptyList()), response);
        verify(taskService, times(1)).getAllTasks();
    }

    @Test
    void getTasks() {
        int page = 0;
        int size = 10;
        Page<TaskShowDto> taskPage = mock(Page.class);

        when(taskService.getTasksByAuthorOrExecutor(null, null, page, size)).thenReturn(taskPage);

        ResponseEntity<Page<TaskShowDto>> response = taskController.getTasks(null, null, page, size);

        assertEquals(ResponseEntity.ok(taskPage), response);
        verify(taskService, times(1)).getTasksByAuthorOrExecutor(null, null, page, size);
    }
}
