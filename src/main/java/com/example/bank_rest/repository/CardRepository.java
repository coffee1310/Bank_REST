package com.example.bank_rest.repository;

import com.example.bank_rest.entity.Card;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> getCardsByUser_Username(String userUsername);
    Optional<Card> getCardById(Long id);
    Optional<Card> getCardByIdAndUser_Username(Long id, String userUsername);
    Boolean existsCardByCardNumber(String cardNumber);
    Boolean existsCardById(Long id);

    @Modifying
    @Query("update Card c set c.status = :status where c.id = :id")
    void setCardStatusById(@Param("status") String status, @Param("id") Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT * FROM FROM Cards WHERE id = :id")
    Optional<Card> findCardByIdWithPessimisticLock(@Param("id") Long id);
}
