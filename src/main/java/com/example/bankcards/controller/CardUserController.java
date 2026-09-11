package com.example.bankcards.controller;

import com.example.bankcards.dto.BlockRequestResponseDTO;
import com.example.bankcards.dto.card.CardResponseDTO;
import com.example.bankcards.entity.User;
import com.example.bankcards.service.BlockRequestService;
import com.example.bankcards.service.card.CardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/cards")
public class CardUserController {

    private final CardService cardService;
    private final BlockRequestService blockRequestService;

    public CardUserController(CardService cardService, BlockRequestService blockRequestService) {
        this.cardService = cardService;
        this.blockRequestService = blockRequestService;
    }

    @GetMapping
    public ResponseEntity<List<CardResponseDTO>> getAllCards(@AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = (User) userDetails;
        List<CardResponseDTO> cards = cardService.getCards(currentUser.getId());

        return ResponseEntity.ok(cards);
    }

    @GetMapping("/{cardId}")
    public ResponseEntity<CardResponseDTO> getCardById(@AuthenticationPrincipal UserDetails userDetails,
                                                       @PathVariable Long cardId) {
        User currentUser = (User) userDetails;
        CardResponseDTO card = cardService.getCardById(cardId, currentUser.getId());
        return ResponseEntity.ok(card);
    }

    @PostMapping("/{cardId}/block-request")
    public ResponseEntity<BlockRequestResponseDTO> blockCard(@AuthenticationPrincipal UserDetails userDetails,
                                                             @PathVariable Long cardId) {
        User user = (User) userDetails;
        BlockRequestResponseDTO response = blockRequestService.requestBlock(cardId, user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
