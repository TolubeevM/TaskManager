package com.example.TaskManager.services;

import com.example.TaskManager.models.CustomUserDetails;
import com.example.TaskManager.models.User;
import com.example.TaskManager.models.enums.Role;
import com.example.TaskManager.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

import static com.example.TaskManager.models.enums.Role.ROLE_ADMIN;
import static com.example.TaskManager.models.enums.Role.ROLE_USER;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;

    public void changeUserRole(String username) {
        User user = userRepository.findByUsername(username);
        Set<Role> roles = user.getRoles();
        if(roles.contains(ROLE_USER))
        {
            roles.clear();
            roles.add(ROLE_ADMIN);
            user.setRoles(roles);
        }else {
            roles.clear();
            roles.add(ROLE_USER);
            user.setRoles(roles);
        }
        userRepository.save(user);
    }
}