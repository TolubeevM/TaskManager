package com.example.TaskManager.controllers;

import com.example.TaskManager.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Autowired
    private final MockMvc mockMvc;

    @Autowired
    private final ObjectMapper objectMapper;

    UserControllerTest() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();
    }

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateNewUser_Failure_UserExists() throws Exception {
        String username = "testUser";
        String email = "test@example.com";
        String password = "password123";

        when(userService.createNewUser(username, email, password)).thenReturn(null);

        mockMvc.perform(post("/user/registration")
                        .param("username", username)
                        .param("email", email)
                        .param("password", password))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User already exists"));
    }
}