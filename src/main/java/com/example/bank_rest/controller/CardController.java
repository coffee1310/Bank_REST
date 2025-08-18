package com.example.bank_rest.controller;

import com.example.bank_rest.dto.CardDTO;
import com.example.bank_rest.entity.Card;
import com.example.bank_rest.exception.CardNotFoundException;
import com.example.bank_rest.exception.UserDoesNotExistException;
import com.example.bank_rest.repository.CardRepository;
import com.example.bank_rest.service.CardService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> getCards() throws UserDoesNotExistException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        List<CardDTO> cards = cardService.getCards(username);
        return new ResponseEntity<>(cards, HttpStatus.OK);
    }

    @PostMapping("/card")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createCard(@RequestBody CardDTO cardDTO) throws Exception {
        CardDTO card = cardService.createCard(cardDTO);

        return new ResponseEntity<>(card, HttpStatus.OK);
    }

    @GetMapping("/card/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getCard(@PathVariable Long id ) throws CardNotFoundException {
        CardDTO card = cardService.getCard(id)
                .orElseThrow(() -> new CardNotFoundException("Card with this id was not found"));

        return new ResponseEntity<>(card, HttpStatus.OK);
    }
}