package com.example.bankcards.util;

import com.example.bankcards.dto.CardResponseDTO;
import com.example.bankcards.entity.Card;
import org.springframework.stereotype.Component;

import java.time.YearMonth;

@Component
public class CardMapper {

    public CardResponseDTO toCardResponseDTO(Card card) {
        if (card == null) {
            return null;
        }

        CardResponseDTO cardResponseDTO = new CardResponseDTO();

        cardResponseDTO.setId(card.getId());
        cardResponseDTO.setOwnerId(card.getOwner().getId());
        cardResponseDTO.setCardNumber(maskCardNumber(card.getCardNumber()));
        cardResponseDTO.setExpirationDate(YearMonth.from(card.getExpirationDate()));
        cardResponseDTO.setCardStatus(card.getCardStatus());
        cardResponseDTO.setBalance(card.getBalance());

        return cardResponseDTO;
    }

    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || !cardNumber.matches("\\d{4}-\\d{4}-\\d{4}-\\d{4}")) {
            return null;
        }

        int length = cardNumber.length();

        String lastFourDigits = cardNumber.substring(length - 4);
        return "**** **** **** " + lastFourDigits;
    }
}
