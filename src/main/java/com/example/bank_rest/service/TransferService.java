package com.example.bank_rest.service;

import com.example.bank_rest.dto.CardDTO;
import com.example.bank_rest.dto.TransferDTO;
import com.example.bank_rest.entity.Card;
import com.example.bank_rest.entity.Transfer;
import com.example.bank_rest.repository.CardRepository;
import com.example.bank_rest.repository.TransferRepository;
import com.example.bank_rest.util.converter.DTOConverterFactory;
import com.example.bank_rest.util.converter.EntityConverter;
import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Transactional
public class TransferService {

    private final TransferRepository transferRepository;
    public final CardRepository cardRepository;
    private final Validator validator;
    private final DTOConverterFactory converterFactory;

    public Transfer transferMoney(TransferDTO transferDTO) {
        transferDTO.validate(validator);
        Transfer transfer = getConverter().toEntity(transferDTO);
        Card card_from = transfer.getCard_from();
        Card card_to = transfer.getCard_to();

        card_from.withdraw(transfer.getAmount());
        card_to.deposit(transfer.getAmount());

        return transferRepository.save(transfer);
    }

    private EntityConverter<Transfer, TransferDTO> getConverter() {
        return converterFactory.getConverter(Transfer.class, TransferDTO.class);
    }
}
