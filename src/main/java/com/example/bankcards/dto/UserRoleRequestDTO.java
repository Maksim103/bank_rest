package com.example.bankcards.dto;

import com.example.bankcards.entity.enums.Role;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserRoleRequestDTO {

    @NotNull(message = "Role must not be null")
    private Role role;
}
