package com.example.TaskManager.controllers;

import com.example.TaskManager.models.CustomUserDetails;
import com.example.TaskManager.services.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentControllerTest {

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddComment() {
        Long taskId = 1L;
        String text = "Test comment";
        CustomUserDetails userDetails = mock(CustomUserDetails.class);

        doNothing().when(commentService).addCommentToTask(taskId, text, userDetails);

        ResponseEntity<?> response = commentController.addComment(taskId, text, userDetails);

        assertNotNull(response);
        assertEquals("Comment added", response.getBody());
        verify(commentService, times(1)).addCommentToTask(taskId, text, userDetails);
    }

}
