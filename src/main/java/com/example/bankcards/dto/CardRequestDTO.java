package com.example.bankcards.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CardRequestDTO {

    @NotNull(message = "Owner ID cannot be null")
    @Positive(message = "Owner ID must be a positive number")
    private Long ownerId;
}
