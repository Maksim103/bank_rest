package com.example.bankcards.service;

import com.example.bankcards.dto.BlockRequestResponseDTO;
import com.example.bankcards.entity.BlockRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.enums.BlockRequestStatus;
import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.exception.blockRequest.BlockRequestNotFoundException;
import com.example.bankcards.exception.card.CardNotActiveException;
import com.example.bankcards.repository.BlockRequestRepository;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.util.BlockRequestMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminBlockRequestService {

    private final BlockRequestRepository blockRequestRepository;
    private final CardRepository cardRepository;
    private final BlockRequestMapper blockRequestMapper;

    public AdminBlockRequestService(BlockRequestRepository blockRequestRepository, CardRepository cardRepository, BlockRequestMapper blockRequestMapper) {
        this.blockRequestRepository = blockRequestRepository;
        this.cardRepository = cardRepository;
        this.blockRequestMapper = blockRequestMapper;
    }

    public List<BlockRequestResponseDTO> getPendingBlockRequests() {
        return blockRequestRepository.findAllByStatus(BlockRequestStatus.PENDING).stream()
                .map(blockRequestMapper::toBlockRequestResponseDTO)
                .toList();
    }

    @Transactional
    public BlockRequestResponseDTO approveBlockRequest(Long requestId) {
        BlockRequest blockRequest = blockRequestRepository.findByIdAndStatus(requestId, BlockRequestStatus.PENDING)
                .orElseThrow(() -> new BlockRequestNotFoundException("Block request not found"));

        Card card = blockRequest.getCard();

        if (!card.getCardStatus().equals(CardStatus.ACTIVE)) {
            throw new CardNotActiveException("Card is not active");
        }

        card.setCardStatus(CardStatus.BLOCKED);
        blockRequest.setStatus(BlockRequestStatus.APPROVED);

        BlockRequest savedBlockRequest = blockRequestRepository.save(blockRequest);
        cardRepository.save(card);

        return blockRequestMapper.toBlockRequestResponseDTO(savedBlockRequest);
    }

    @Transactional
    public BlockRequestResponseDTO rejectBlockRequest(Long requestId) {
        BlockRequest blockRequest = blockRequestRepository
                .findByIdAndStatus(requestId, BlockRequestStatus.PENDING)
                .orElseThrow(() -> new BlockRequestNotFoundException("Block request not found"));

        blockRequest.setStatus(BlockRequestStatus.REJECTED);
        BlockRequest savedBlockRequest = blockRequestRepository.save(blockRequest);

        return blockRequestMapper.toBlockRequestResponseDTO(savedBlockRequest);
    }
}
