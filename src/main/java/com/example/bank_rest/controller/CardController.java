package com.example.bank_rest.controller;

import com.example.bank_rest.dto.CardDTO;
import com.example.bank_rest.entity.Card;
import com.example.bank_rest.repository.CardRepository;
import com.example.bank_rest.service.CardService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/card")
public class CardController {

    private final CardRepository cardRepository;
    private final CardService cardService;

    @GetMapping("/cards")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> getCards() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        List<Card> cards = cardRepository.getCardsByUser_Username(username);
        return new ResponseEntity<>(cards, HttpStatusCode.valueOf(200));
    }

    @PostMapping("/card")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createCard(@RequestBody CardDTO cardDTO) throws Exception {
        Card card = cardService.createCard(cardDTO);

        return new ResponseEntity<>(card, HttpStatusCode.valueOf(200));
    }
}