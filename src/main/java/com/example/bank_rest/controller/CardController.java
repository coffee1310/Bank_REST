package com.example.bank_rest.controller;

import com.example.bank_rest.dto.CardDTO;
import com.example.bank_rest.entity.Card;
import com.example.bank_rest.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/card")
public class CardController {

    private final CardRepository cardRepository;

    @GetMapping("/cards")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getCards() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        List<Card> cards = cardRepository.getCardsByUser_Username(username);
        return new ResponseEntity<>(cards, HttpStatusCode.valueOf(200));
    }

//    @PostMapping
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<?> createCard(@RequestBody CardDTO cardDTO) {
//    }
}
