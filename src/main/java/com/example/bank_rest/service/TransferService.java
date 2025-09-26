package com.example.bank_rest.service;

import com.example.bank_rest.dto.CardDTO;
import com.example.bank_rest.dto.TransferDTO;
import com.example.bank_rest.entity.Card;
import com.example.bank_rest.entity.Transfer;
import com.example.bank_rest.exception.CardDoesNotFound;
import com.example.bank_rest.repository.CardRepository;
import com.example.bank_rest.repository.TransferRepository;
import com.example.bank_rest.util.converter.DTOConverterFactory;
import com.example.bank_rest.util.converter.EntityConverter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
@Transactional
public class TransferService {

    private final TransferRepository transferRepository;
    private final Validator validator;
    private final DTOConverterFactory converterFactory;
    private final CardRepository cardRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public TransferDTO transferMoney(TransferDTO transferDTO) throws OptimisticLockingFailureException {
//        transferDTO.setCreated_at(LocalDateTime.now().minusMinutes(1));
//        transferDTO.validate(validator);
//        Transfer transfer = getConverter().toEntity(transferDTO);
//        Card card_from = transfer.getCard_from();
//        Card card_to = transfer.getCard_to();
//
//        card_from.withdraw(transfer.getAmount());
//        card_to.deposit(transfer.getAmount());
//
//        return transferRepository.save(transfer);

        Long fromCardId = transferDTO.getCard_from_id();
        Long toCardId = transferDTO.getCard_to_id();
        BigDecimal amount = transferDTO.getAmount();

        List<Long> sortedIds = Stream.of(fromCardId, toCardId)
                .sorted()
                .collect(Collectors.toList());;

        Card card_from = cardRepository.findCardByIdWithPessimisticLock(sortedIds.get(0))
                .orElseThrow(() -> new CardDoesNotFound("Card with this is was not found"));

        Card card_to = cardRepository.findCardByIdWithPessimisticLock(sortedIds.get(1))
                .orElseThrow(() -> new CardDoesNotFound("Card with this is was not found"));

        card_from.withdraw(amount);
        card_to.deposit(amount);

        return transferDTO;
    }

    private EntityConverter<Transfer, TransferDTO> getConverter() {
        return converterFactory.getConverter(Transfer.class, TransferDTO.class);
    }
}
