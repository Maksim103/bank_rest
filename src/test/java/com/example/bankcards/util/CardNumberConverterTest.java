package com.example.bankcards.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

public class CardNumberConverterTest {
    private CardNumberConverter converter;

    @BeforeEach
    void setUp() {
        converter = new CardNumberConverter();

        String testRawKey = Base64.getEncoder().encodeToString("TestSecretKeyMustBe32BytesLong!!".getBytes());
        ReflectionTestUtils.setField(converter, "secretKey", testRawKey);
        converter.init();
    }

    @Test
    void shouldEncryptAndDecryptSuccessfully() {
        String originalCardNumber = "4111-2222-3333-4444";
        String encryptedDbData = converter.convertToDatabaseColumn(originalCardNumber);

        assertNotNull(encryptedDbData);
        assertNotEquals(originalCardNumber, encryptedDbData);
        assertFalse(encryptedDbData.contains("-"));

        String decryptedEntityAttribute = converter.convertToEntityAttribute(encryptedDbData);

        assertEquals(originalCardNumber, decryptedEntityAttribute);
    }

    @Test
    void shouldProduceDifferentCipherTextForSameInput() {
        String cardNumber = "1234-5678-9012-3456";

        String encryptionOne = converter.convertToDatabaseColumn(cardNumber);
        String encryptionTwo = converter.convertToDatabaseColumn(cardNumber);

        assertNotEquals(encryptionOne, encryptionTwo);
    }

    @Test
    void shouldReturnNullWhenInputIsNull() {
        assertNull(converter.convertToDatabaseColumn(null));
        assertNull(converter.convertToEntityAttribute(null));
    }
}
