package com.example.bankcards.controller;

import com.example.bankcards.dto.UserResponseDTO;
import com.example.bankcards.dto.UserRoleRequestDTO;
import com.example.bankcards.entity.User;
import com.example.bankcards.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<UserResponseDTO> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long userId) {
        UserResponseDTO user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/{userId}/role")
    public ResponseEntity<UserResponseDTO> changeUserRole(
            @PathVariable Long userId,
            @RequestBody @Valid UserRoleRequestDTO userRoleRequestDTO) {

        UserResponseDTO user = userService.changeUserRole(userId, userRoleRequestDTO.getRole());
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId, @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = (User) userDetails;
        Long currentUserId = currentUser.getId();

        userService.deleteUser(userId, currentUserId);
        return ResponseEntity.noContent().build();
    }
}
