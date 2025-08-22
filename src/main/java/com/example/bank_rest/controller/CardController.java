package com.example.bank_rest.controller;

import com.example.bank_rest.dto.CardDTO;
import com.example.bank_rest.exception.CardNotFoundException;
import com.example.bank_rest.exception.UserDoesNotExistException;
import com.example.bank_rest.repository.CardRepository;
import com.example.bank_rest.service.CardService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/card")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class CardController {

    private final CardService cardService;
    private final CardRepository cardRepository;

    @GetMapping
    public ResponseEntity<?> getCards() throws UserDoesNotExistException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        List<CardDTO> cards = cardService.getCards(username);
        return new ResponseEntity<>(cards, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createCard(@RequestBody CardDTO cardDTO) throws Exception {
        CardDTO card = cardService.createCard(cardDTO);

        return new ResponseEntity<>(card, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getCard(@PathVariable Long id) throws CardNotFoundException {
        CardDTO card = cardService.getCard(id);
        return new ResponseEntity<>(card, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> blockCard(@PathVariable Long id) throws CardNotFoundException {
        CardDTO card = cardService.setCardStatus(id, "BLOCKED");
        return new ResponseEntity<>(card, HttpStatus.OK);
    }

    @GetMapping("/{id}/number")
    public ResponseEntity<?> getCardNumber(@PathVariable Long id) throws CardNotFoundException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        CardDTO cardDTO = cardService.getCardByIdAndUsername(id, username);
        return new ResponseEntity<>(cardDTO.getMaskedNumber(), HttpStatus.OK);
    }

    @GetMapping("/{id}/balance")
    public ResponseEntity<?> getCardBalance(@PathVariable Long id) throws CardNotFoundException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        CardDTO cardDTO = cardService.getCardByIdAndUsername(id, username);
        return new ResponseEntity<>(cardDTO.getBalance().toString(), HttpStatus.OK);
    }


}