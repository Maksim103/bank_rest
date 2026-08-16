package com.example.bankcards.repository;

import com.example.bankcards.entity.User;
import com.example.bankcards.entity.enums.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.DataIntegrityViolationException;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mysql.MySQLContainer;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Container
    @ServiceConnection
    private static MySQLContainer mySQLContainer = new MySQLContainer("mysql:8.4.11");

    @Test
    void shouldSaveAndFindUser() {
        User user = new User("Maks", "12345", Role.USER);

        User savedUser = userRepository.save(user);

        assertThat(savedUser.getId()).isNotNull();

        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("Maks");
        assertThat(foundUser.get().getPassword()).isEqualTo("12345");
        assertThat(foundUser.get().getRole()).isEqualTo(Role.USER);
    }

    @Test
    void shouldThrowExceptionWhenNameIsDuplicate() {
        User user1 = new User("Maks", "12345", Role.USER);
        userRepository.save(user1);

        User user2 = new User("Maks", "5241", Role.ADMIN);

        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> {
                    userRepository.saveAndFlush(user2);
                });
    }

    @Test
    void shouldReturnEmptyOptionalWhenIdDoesNotExist() {
        Long nonExistentId = Long.MAX_VALUE;

        Optional<User> user = userRepository.findById(nonExistentId);

        assertThat(user).isEmpty();
    }

    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        User userWithNullName = new User(null, "12345", Role.USER);

        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> userRepository.saveAndFlush(userWithNullName));
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsNull() {
        User userWithNullPassword = new User("Maks", null, Role.ADMIN);

        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> userRepository.saveAndFlush(userWithNullPassword));
    }

    @Test
    void shouldThrowExceptionWhenRoleIsNull() {
        User userWithNullRole = new User("Maks", "7902343", null);

        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> userRepository.saveAndFlush(userWithNullRole));
    }
}
