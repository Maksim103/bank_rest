package com.example.bankcards.controller;

import com.example.bankcards.dto.login.LoginRequestDTO;
import com.example.bankcards.dto.login.LoginResponseDTO;
import com.example.bankcards.dto.register.RegisterRequestDTO;
import com.example.bankcards.dto.register.RegisterResponseDTO;
import com.example.bankcards.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> registerUser(@RequestBody @Valid RegisterRequestDTO registerRequestDTO) {
        RegisterResponseDTO responseDTO = authService.registerUser(registerRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginUser(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        LoginResponseDTO response = authService.loginUser(loginRequestDTO);
        return ResponseEntity.ok(response);
    }
}
