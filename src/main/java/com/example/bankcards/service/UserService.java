package com.example.bankcards.service;

import com.example.bankcards.dto.UserResponseDTO;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.enums.Role;
import com.example.bankcards.exception.user.UserNotFoundException;
import com.example.bankcards.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public Page<UserResponseDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(user -> new UserResponseDTO(user.getId(), user.getUsername(), user.getRole()));
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(user -> new UserResponseDTO(user.getId(), user.getUsername(), user.getRole()))
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Transactional
    public UserResponseDTO changeUserRole(Long id, Role newRole) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (user.getRole() == newRole) {
            throw new IllegalArgumentException("User already has the role: " + newRole);
        }

        if (user.getRole().equals(Role.ADMIN)
                && newRole.equals(Role.USER)
                && userRepository.countUserByRole(Role.ADMIN) <= 1) {
            throw new IllegalStateException("Cannot change role. There must be at least one admin user.");
        }

        user.setRole(newRole);

        return new UserResponseDTO(user.getId(), user.getUsername(), user.getRole());
    }

    @Transactional
    public void deleteUser(Long targetId, Long currentUserId) {
        if (targetId.equals(currentUserId)) {
            throw new IllegalArgumentException("Admin cannot delete themselves");
        }

        User user = userRepository.findById(targetId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (userRepository.existsUserByIdAndCardsNotEmpty(targetId)) {
            throw new IllegalStateException("Cannot delete user with associated cards");
        }

        if (user.getRole().equals(Role.ADMIN)
                && userRepository.countUserByRole(Role.ADMIN) <= 1) {
            throw new IllegalStateException("Cannot delete the last admin user");
        }

        userRepository.deleteById(targetId);
    }
}
