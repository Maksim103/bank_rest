package com.example.bankcards.service;

import com.example.bankcards.dto.login.LoginRequestDTO;
import com.example.bankcards.dto.login.LoginResponseDTO;
import com.example.bankcards.dto.register.RegisterRequestDTO;
import com.example.bankcards.dto.register.RegisterResponseDTO;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.enums.Role;
import com.example.bankcards.exception.InvalidCredentialsException;
import com.example.bankcards.exception.UsernameAlreadyExistsException;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public RegisterResponseDTO registerUser(RegisterRequestDTO registerRequestDTO) {
        String username = registerRequestDTO.getUsername();
        String password = registerRequestDTO.getPassword();

        if (userRepository.existsUserByUsername(username)) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.USER);

        User savedUser = userRepository.save(user);

        return new RegisterResponseDTO(savedUser.getId(), savedUser.getUsername(), savedUser.getRole());
    }

    public LoginResponseDTO loginUser(LoginRequestDTO loginRequestDTO) {
        String username = loginRequestDTO.getUsername();
        String password = loginRequestDTO.getPassword();

        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        String token = jwtService.generateToken(user.getId(), user.getUsername(), user.getRole());

        return new LoginResponseDTO(token);
    }
}
