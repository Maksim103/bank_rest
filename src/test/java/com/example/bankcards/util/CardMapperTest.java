package com.example.bankcards.util;

import com.example.bankcards.dto.CardResponseDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.entity.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CardMapperTest {

    private CardMapper cardMapper;
    private User ownerCard;

    @BeforeEach
    public void setUp() {
        cardMapper = new CardMapper();
        ownerCard = new User("Maks", "2141", Role.USER);
        ownerCard.setId(1L);
    }

    @Test
    void shouldMapCardToCardResponseDTO() {
        Card card = new Card("4234-2312-2234-5212", ownerCard, LocalDate.of(2026, Month.SEPTEMBER, 15), CardStatus.ACTIVE, BigDecimal.TEN, false);
        card.setId(1L);

        CardResponseDTO response = cardMapper.toCardResponseDTO(card);

        assertEquals(card.getId(), response.getId());
        assertEquals("**** **** **** 5212", response.getCardNumber());
        assertEquals(card.getOwner().getId(), response.getOwnerId());
        assertEquals(YearMonth.from(card.getExpirationDate()), response.getExpirationDate());
        assertEquals(card.getCardStatus(), response.getCardStatus());
        assertEquals(card.getBalance(), response.getBalance());
    }

    @Test
    void shouldReturnNullWhenCardIsNull() {
        CardResponseDTO response = cardMapper.toCardResponseDTO(null);

        assertNull(response);
    }

    @Test
    void shouldReturnNullCardNumberWhenCardNumberHasInvalidFormat() {
        Card card = new Card("invalid-card-number", ownerCard, LocalDate.of(2026, Month.SEPTEMBER, 15), CardStatus.ACTIVE, BigDecimal.TEN, false);

        CardResponseDTO response = cardMapper.toCardResponseDTO(card);

        assertNull(response.getCardNumber());
    }
}
