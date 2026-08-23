package com.example.bankcards.dto.register;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {

    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters long")
    @NotBlank(message = "Username cannot be blank")
    private String username;

    @Size(min = 8, max = 50, message = "Password must be between 8 and 50 characters long")
    @NotBlank(message = "Password cannot be blank")
    private String password;

}
