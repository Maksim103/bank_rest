package com.example.bankcards.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.YearMonth;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CardCreateRequestDTO {

    @NotNull(message = "Owner ID cannot be null")
    @Positive(message = "Owner ID must be a positive number")
    private Long ownerId;

    @Pattern(regexp = "\\d{4}-\\d{4}-\\d{4}-\\d{4}",
            message = "Card number must be in the format XXXX-XXXX-XXXX-XXXX")
    @NotBlank(message = "Card number cannot be blank")
    private String cardNumber;

    @DateTimeFormat(pattern = "MM/yy")
    @NotNull(message = "Expiration date cannot be null")
    @Future
    private YearMonth expirationDate;
}
