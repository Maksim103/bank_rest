package com.example.bankcards.service.card;

import com.example.bankcards.dto.card.CardResponseDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.exception.card.CardNotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.util.CardMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardUserService {

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;

    public CardUserService(CardRepository cardRepository, CardMapper cardMapper) {
        this.cardRepository = cardRepository;
        this.cardMapper = cardMapper;
    }

    public List<CardResponseDTO> getCards(Long ownerId) {
        return cardRepository.findAll().stream()
                .filter(card -> card.getOwner().getId().equals(ownerId))
                .map(cardMapper::toCardResponseDTO)
                .toList();
    }

    public CardResponseDTO getCardById(Long cardId, Long ownerId) {
        Card card = cardRepository.findById(cardId)
                .filter(cardOwner -> cardOwner.getOwner().getId().equals(ownerId))
                .orElseThrow(() -> new CardNotFoundException("Card not found"));


        return cardMapper.toCardResponseDTO(card);
    }
}