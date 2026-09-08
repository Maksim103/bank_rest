package com.example.bankcards.dto.card;

import com.example.bankcards.entity.enums.CardStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.YearMonth;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CardResponseDTO {

    private Long id;
    private Long ownerId;
    private String cardNumber;
    private YearMonth expirationDate;
    private CardStatus cardStatus;
    private BigDecimal balance;

}
