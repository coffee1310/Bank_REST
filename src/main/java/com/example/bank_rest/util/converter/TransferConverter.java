package com.example.bank_rest.util.converter;

import com.example.bank_rest.dto.CardDTO;
import com.example.bank_rest.dto.TransferDTO;
import com.example.bank_rest.entity.Card;
import com.example.bank_rest.entity.Transfer;
import com.example.bank_rest.entity.User;
import com.example.bank_rest.exception.CardDoesNotFound;
import com.example.bank_rest.exception.UserDoesNotExistException;
import com.example.bank_rest.repository.CardRepository;
import com.example.bank_rest.repository.TransferRepository;
import com.example.bank_rest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransferConverter implements EntityConverter<Transfer, TransferDTO>{
    private final TransferRepository transferRepository;
    private final CardRepository cardRepository;

    @Override
    public Transfer toEntity(TransferDTO dto) throws UserDoesNotExistException {
        Card card_from = getCard(dto.getCard_from_id());
        Card card_to =  getCard(dto.getCard_to_id());

        Transfer transfer = new Transfer();
        transfer.setId(dto.getId());
        transfer.setAmount(dto.getAmount());
        transfer.setCard_from(card_from);
        transfer.setCard_to(card_to);
        transfer.setCreated_at(dto.getCreated_at());
        return transfer;
    }

    @Override
    public TransferDTO toDto(Transfer transfer) throws UserDoesNotExistException {
        TransferDTO transferDTO = new TransferDTO();
        transferDTO.setId(transfer.getId());
        transferDTO.setCard_from_id(transfer.getCard_from().getId());
        transferDTO.setCard_to_id(transfer.getCard_to().getId());
        transferDTO.setAmount(transfer.getAmount());
        transferDTO.setCreated_at(transfer.getCreated_at());

        return transferDTO;
    }

    private Card getCard(Long id) throws CardDoesNotFound {
        return cardRepository.getCardById(id).orElseThrow(() -> new CardDoesNotFound("Card with this id doesn't exist"));
    }

}
