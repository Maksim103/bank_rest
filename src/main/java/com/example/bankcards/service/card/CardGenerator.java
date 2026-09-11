package com.example.bankcards.service.card;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;

@Service
public class CardGenerator {

    private final SecureRandom random;

    public CardGenerator() {
        random = new SecureRandom();
    }

    public String generateCardNumber() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 16; i++) {
            int digit = random.nextInt(10);
            sb.append(digit);

            if ((i + 1) % 4 == 0 && i != 15) {
                sb.append("-");
            }
        }

        return sb.toString();
    }

    public LocalDate generateExpirationDate() {
        return LocalDate.now().plusYears(5);
    }
}
