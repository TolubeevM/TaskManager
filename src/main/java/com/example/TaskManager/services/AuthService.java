package com.example.TaskManager.services;

import com.example.TaskManager.models.User;
import com.example.TaskManager.utils.JwtTokenUtils;
import com.example.TaskManager.repositories.UserRepository;
import com.example.TaskManager.models.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    @Lazy
    private final UserService userService;

    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;


    public String createAuthToken(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found with email: " + email);
        }

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), password));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect login or password");
        }

        CustomUserDetails customUserDetails = userService.loadUserByUsername(user.getUsername());
        return jwtTokenUtils.generateToken(customUserDetails);
    }
}