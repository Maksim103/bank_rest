package com.example.bankcards.controller;

import com.example.bankcards.dto.card.CardRequestDTO;
import com.example.bankcards.dto.card.CardResponseDTO;
import com.example.bankcards.service.card.CardAdminService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/cards")
public class CardAdminController {

    private final CardAdminService cardAdminService;

    public CardAdminController(CardAdminService cardAdminService) {
        this.cardAdminService = cardAdminService;
    }

    @PostMapping
    public ResponseEntity<CardResponseDTO> createCard(@RequestBody @Valid CardRequestDTO cardRequestDTO) {
        CardResponseDTO responseDTO = cardAdminService.createCard(cardRequestDTO.getOwnerId());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("/block/{cardId}")
    public ResponseEntity<CardResponseDTO> blockCard(@PathVariable Long cardId) {
        return ResponseEntity.ok(cardAdminService.blockCard(cardId));
    }

    @PutMapping("/activate/{cardId}")
    public ResponseEntity<CardResponseDTO> activateCard(@PathVariable Long cardId) {
        return ResponseEntity.ok(cardAdminService.activateCard(cardId));
    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long cardId) {
        cardAdminService.deleteCard(cardId);
        return ResponseEntity.noContent().build();
    }
}
