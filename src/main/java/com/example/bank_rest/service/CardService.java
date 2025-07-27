package com.example.bank_rest.service;

import com.example.bank_rest.entity.Card;
import com.example.bank_rest.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;

    public List<Card> getAllCards(String username) {
        return cardRepository.getCardsByUser_Username(username);
    }
}
