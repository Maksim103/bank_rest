package com.example.bankcards.service;

import com.example.bankcards.dto.BlockRequestResponseDTO;
import com.example.bankcards.entity.BlockRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.enums.BlockRequestStatus;
import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.exception.blockRequest.BlockRequestAlreadyExistsException;
import com.example.bankcards.exception.card.CardNotFoundException;
import com.example.bankcards.repository.BlockRequestRepository;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.util.BlockRequestMapper;
import org.springframework.stereotype.Service;

@Service
public class BlockRequestService {

    private final CardRepository cardRepository;
    private final BlockRequestRepository blockRequestRepository;
    private final BlockRequestMapper blockRequestMapper;

    public BlockRequestService(CardRepository cardRepository, BlockRequestRepository blockRequestRepository, BlockRequestMapper blockRequestMapper) {
        this.cardRepository = cardRepository;
        this.blockRequestRepository = blockRequestRepository;
        this.blockRequestMapper = blockRequestMapper;
    }

    public BlockRequestResponseDTO requestBlock(Long cardId, Long ownerId) {
        Card card = cardRepository.findById(cardId)
                .filter(cardOwner -> cardOwner.getOwner().getId().equals(ownerId))
                .filter(cardStatus -> cardStatus.getCardStatus().equals(CardStatus.ACTIVE))
                .orElseThrow(() -> new CardNotFoundException("Card not found"));

        if (blockRequestRepository.existsByCardAndStatus(card, BlockRequestStatus.PENDING)) {
            throw new BlockRequestAlreadyExistsException("A pending block request already exists for this card");
        }

        BlockRequest blockRequest = new BlockRequest(card, card.getOwner(), BlockRequestStatus.PENDING);

        BlockRequest savedBlockRequest = blockRequestRepository.save(blockRequest);
        return blockRequestMapper.toBlockRequestResponseDTO(savedBlockRequest);
    }
}
