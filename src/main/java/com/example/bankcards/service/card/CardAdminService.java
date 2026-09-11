package com.example.bankcards.service.card;

import com.example.bankcards.dto.card.CardResponseDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.exception.card.CardNotFoundException;
import com.example.bankcards.exception.user.UserNotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.CardMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CardAdminService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final CardMapper cardMapper;
    private final CardGenerator cardGenerator;

    public CardAdminService(CardRepository cardRepository,
                            UserRepository userRepository,
                            CardMapper cardMapper,
                            CardGenerator cardGenerator) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
        this.cardMapper = cardMapper;
        this.cardGenerator = cardGenerator;
    }

    public CardResponseDTO createCard(Long ownerId) {
        User ownerCard = userRepository.findById(ownerId).orElseThrow(() ->
                new UserNotFoundException("User with ID " + ownerId + " does not exist"));

        Card newCard = new Card();
        newCard.setOwner(ownerCard);

        do {
            newCard.setCardNumber(cardGenerator.generateCardNumber());
        } while (cardRepository.existsByCardNumber(newCard.getCardNumber()));

        newCard.setExpirationDate(cardGenerator.generateExpirationDate());
        newCard.setCardStatus(CardStatus.ACTIVE);
        newCard.setBalance(BigDecimal.ZERO);

        Card savedCard = cardRepository.save(newCard);

        return cardMapper.toCardResponseDTO(savedCard);
    }

    public CardResponseDTO blockCard(Long cardId) {
        Card card = cardRepository.findById(cardId).orElseThrow(() ->
                new CardNotFoundException("Card with ID " + cardId + " does not exist"));

        switch (card.getCardStatus()) {
            case BLOCKED:
                throw new IllegalStateException("Card is already blocked");
            case EXPIRED:
                throw new IllegalStateException("Cannot block an expired card");
            case ACTIVE:
                card.setCardStatus(CardStatus.BLOCKED);
                break;
        }

        Card savedCard = cardRepository.save(card);

        return cardMapper.toCardResponseDTO(savedCard);
    }

    public CardResponseDTO activateCard(Long cardId) {
        Card card = cardRepository.findById(cardId).orElseThrow(() ->
                new CardNotFoundException("Card with ID " + cardId + " does not exist"));

        switch (card.getCardStatus()) {
            case BLOCKED:
                card.setCardStatus(CardStatus.ACTIVE);
                break;
            case EXPIRED:
                throw new IllegalStateException("Cannot activate an expired card");
            case ACTIVE:
                throw new IllegalStateException("Card is already active");
        }

        Card savedCard = cardRepository.save(card);

        return cardMapper.toCardResponseDTO(savedCard);
    }

    public void deleteCard(Long cardId) {
        Card card = cardRepository.findById(cardId).orElseThrow(() ->
                new CardNotFoundException("Card with ID " + cardId + " does not exist"));

        cardRepository.delete(card);
    }
}
