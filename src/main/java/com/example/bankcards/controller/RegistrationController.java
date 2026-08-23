package com.example.bankcards.controller;

import com.example.bankcards.dto.RegisterRequestDTO;
import com.example.bankcards.dto.RegisterResponseDTO;
import com.example.bankcards.exception.UsernameAlreadyExistsException;
import com.example.bankcards.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> registerUser(@RequestBody @Valid RegisterRequestDTO registerRequestDTO) {
        try {
            RegisterResponseDTO responseDTO = userService.registerUser(registerRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (UsernameAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}
