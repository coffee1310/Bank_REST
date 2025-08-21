package com.example.bank_rest.repository;

import com.example.bank_rest.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> getCardsByUser_Username(String userUsername);
    Optional<Card> getCardsById(Long id);
    Boolean existsCardByCardNumber(String cardNumber);

    @Modifying
    @Query("update Card c set c.status = ?1 where c.id = ?2")
    Optional<Card> setCardStatusById(String status, Long id);
}
