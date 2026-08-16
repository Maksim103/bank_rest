package com.example.bankcards.repository;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.entity.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.DataIntegrityViolationException;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mysql.MySQLContainer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class CardRepositoryTest {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Container
    @ServiceConnection
    private static MySQLContainer mySQLContainer = new MySQLContainer("mysql:8.4.11");

    private User ownerCard;

    @BeforeEach
    void setUpOwner() {
        ownerCard = new User("Maks", "2141", Role.USER);
        userRepository.save(ownerCard);
    }

    @Test
    void shouldSaveAndFindCard() {
        Card card = new Card("1234", ownerCard, LocalDate.of(2026, Month.AUGUST, 15), CardStatus.ACTIVE, BigDecimal.TEN, false);
        Card savedCard = cardRepository.save(card);

        assertThat(savedCard.getId()).isNotNull();

        Optional<Card> foundCard = cardRepository.findById(savedCard.getId());

        assertThat(foundCard).isPresent();
        assertThat(foundCard.get().getCardNumber()).isEqualTo("1234");
        assertThat(foundCard.get().getOwner()).isEqualTo(ownerCard);
        assertThat(foundCard.get().getExpirationDate()).isEqualTo(LocalDate.of(2026, Month.AUGUST, 15));
        assertThat(foundCard.get().getCardStatus()).isEqualTo(CardStatus.ACTIVE);
        assertThat(foundCard.get().getBalance()).isEqualTo(BigDecimal.TEN);
        assertThat(foundCard.get().isDeleted()).isEqualTo(false);
    }

    @Test
    void shouldThrowExceptionWhenCardNumberIsDuplicate() {
        Card card1 = new Card("1234", ownerCard, LocalDate.of(2026, Month.AUGUST, 15), CardStatus.ACTIVE, BigDecimal.TEN, false);
        Card card2 = new Card("1234", ownerCard, LocalDate.of(2025, Month.APRIL, 22), CardStatus.EXPIRED, BigDecimal.ONE, false);

        cardRepository.save(card1);

        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> {
                    cardRepository.saveAndFlush(card2);
                });
    }

    @Test
    void shouldReturnEmptyOptionalWhenIdDoesNotExist() {
        Long nonExistentId = Long.MAX_VALUE;

        Optional<Card> card = cardRepository.findById(nonExistentId);

        assertThat(card).isEmpty();
    }

    @Test
    void shouldThrowExceptionWhenCardNumberIsNull() {
        Card cardWithNullCardNumber = new Card(null, ownerCard, LocalDate.of(2026, Month.AUGUST, 15), CardStatus.ACTIVE, BigDecimal.TEN, false);

        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> cardRepository.saveAndFlush(cardWithNullCardNumber));
    }

    @Test
    void shouldThrowExceptionWhenOwnerIsNull() {
        Card cardWithNullOwner = new Card("123456", null, LocalDate.of(2026, Month.AUGUST, 15), CardStatus.ACTIVE, BigDecimal.TEN, false);

        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> cardRepository.saveAndFlush(cardWithNullOwner));
    }

    @Test
    void shouldThrowExceptionWhenExpirationDateIsNull() {
        Card cardWithNullExpirationDate = new Card("1234561", ownerCard, null, CardStatus.ACTIVE, BigDecimal.TEN, false);

        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> cardRepository.saveAndFlush(cardWithNullExpirationDate));
    }

    @Test
    void shouldThrowExceptionWhenCardStatusIsNull() {
        Card cardWithNullCardStatus = new Card("1234561", ownerCard, LocalDate.of(2026, Month.AUGUST, 15), null, BigDecimal.TEN, false);

        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> cardRepository.saveAndFlush(cardWithNullCardStatus));
    }

    @Test
    void shouldThrowExceptionWhenBalanceIsNull() {
        Card cardWithNullBalance = new Card("1234561", ownerCard, LocalDate.of(2026, Month.AUGUST, 15), CardStatus.ACTIVE, null, false);

        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> cardRepository.saveAndFlush(cardWithNullBalance));
    }

    @Test
    void shouldSetDeletedFlagAndHideFromFindAllWhenSoftDeleteExecuted() {
        Card card = new Card("1234561", ownerCard, LocalDate.of(2026, Month.AUGUST, 15), CardStatus.ACTIVE, BigDecimal.TEN, false);
        Card savedCard = entityManager.persistAndFlush(card);

        Long cardId = savedCard.getId();

        cardRepository.delete(card);
        entityManager.flush();
        entityManager.clear();

        Optional<Card> foundCardViaRepository = cardRepository.findById(cardId);
        assertThat(foundCardViaRepository).isEmpty();

        Number result = (Number) entityManager.getEntityManager()
                .createNativeQuery("SELECT is_deleted FROM cards WHERE id = :id")
                .setParameter("id", cardId)
                .getSingleResult();

        boolean isDeleted = (result != null) && (result.intValue() == 1);

        assertThat(isDeleted).isTrue();
    }

    @Test
    void shouldThrowExceptionWhenDeleteUserWithExistingCard() {
        User owner = new User("Oleg", "5512", Role.USER);
        userRepository.save(owner);

        Card card1 = new Card("1234561", owner, LocalDate.of(2026, Month.AUGUST, 15), CardStatus.ACTIVE, BigDecimal.TEN, false);
        Card card2 = new Card("421451", owner, LocalDate.of(2025, Month.OCTOBER, 5), CardStatus.EXPIRED, BigDecimal.ZERO, false);

        cardRepository.save(card1);
        cardRepository.save(card2);

        entityManager.clear();

        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> {
                    userRepository.delete(owner);
                    userRepository.flush();
                });
    }
}
