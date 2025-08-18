package com.example.bank_rest.repository;

import com.example.bank_rest.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> getCardsByUser_Username(String userUsername);
    Card getCardsById(Long id);
    Boolean existsCardByCardNumber(String cardNumber);
}
