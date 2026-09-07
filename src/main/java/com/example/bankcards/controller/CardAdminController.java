package com.example.bankcards.controller;

import com.example.bankcards.dto.CardRequestDTO;
import com.example.bankcards.dto.CardResponseDTO;
import com.example.bankcards.service.CardService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/cards")
public class CardAdminController {

    private final CardService cardService;

    public CardAdminController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping
    public ResponseEntity<CardResponseDTO> createCard(@RequestBody @Valid CardRequestDTO cardRequestDTO) {
        CardResponseDTO responseDTO = cardService.createCard(cardRequestDTO.getOwnerId());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("/block/{cardId}")
    public ResponseEntity<CardResponseDTO> blockCard(@PathVariable Long cardId) {
        return ResponseEntity.ok(cardService.blockCard(cardId));
    }

    @PutMapping("/activate/{cardId}")
    public ResponseEntity<CardResponseDTO> activateCard(@PathVariable Long cardId) {
        return ResponseEntity.ok(cardService.activateCard(cardId));
    }

    @DeleteMapping("/delete/{cardId}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long cardId) {
        cardService.deleteCard(cardId);
        return ResponseEntity.noContent().build();
    }
}
