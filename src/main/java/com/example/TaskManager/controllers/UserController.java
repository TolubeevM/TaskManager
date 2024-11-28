package com.example.TaskManager.controllers;

import com.example.TaskManager.dtos.AuthDto;
import com.example.TaskManager.dtos.SafeLoginDto;
import com.example.TaskManager.models.CustomUserDetails;
import com.example.TaskManager.models.User;
import com.example.TaskManager.repositories.UserRepository;
import com.example.TaskManager.services.AdminService;
import com.example.TaskManager.services.AuthService;
import com.example.TaskManager.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/user")
@Tag(name = "Users", description = "Операции с пользователями")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;


    private final UserService userService;


    private final AuthService authService;

    private final AdminService adminService;


    @Operation(summary = "Авторизация", description = "Авторизует пользователя и возвращает токен")
    @PostMapping("/login")
    public ResponseEntity<?> auth(
            @RequestBody @Parameter(description = "Данные для авторизации (email и пароль)") SafeLoginDto safeLoginDto) {
        try {
            String token = authService.createAuthToken(safeLoginDto.getEmail(), safeLoginDto.getPassword());
            User user = userRepository.findByEmail(safeLoginDto.getEmail());
            return ResponseEntity.ok(new AuthDto(user, token));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неправильный логин или пароль");
        }
    }

    @Operation(summary = "Регистрация", description = "Создает нового пользователя в системе")
    @PostMapping("/registration")
    public ResponseEntity<?> createNewUser(
            @RequestParam(name = "username") @Parameter(description = "Имя пользователя") String username,
            @RequestParam(name = "email") @Parameter(description = "Email пользователя") String email,
            @RequestParam(name = "password") @Parameter(description = "Пароль пользователя") String password) {
        User user = userService.createNewUser(username, email, password);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists");
        }
        return ResponseEntity.ok(userService.registration(email, password, user, authService));
    }

    @Operation(summary = "Изменить роль пользователя", description = "Изменяет роль пользователя (доступно только администратору)")
    @PutMapping("/admin/changeRole/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> changeUserRole(
            @PathVariable @Parameter(description = "Имя пользователя") String username) {
        adminService.changeUserRole(username);
        return ResponseEntity.ok("Role changed successfully");
    }
}

