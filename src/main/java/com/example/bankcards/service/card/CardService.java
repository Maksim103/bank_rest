package com.example.bankcards.service.card;

import com.example.bankcards.dto.card.CardResponseDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.exception.card.CardNotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.util.CardMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardService {

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;
    private final CardAdminService cardAdminService;

    public CardService(CardRepository cardRepository, CardMapper cardMapper, CardAdminService cardAdminService) {
        this.cardRepository = cardRepository;
        this.cardMapper = cardMapper;
        this.cardAdminService = cardAdminService;
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