package com.example.bank_rest.service;

import com.example.bank_rest.dto.CardDTO;
import com.example.bank_rest.entity.Card;
import com.example.bank_rest.exception.UserDoesNotExistException;
import com.example.bank_rest.repository.CardRepository;
import com.example.bank_rest.util.Encryptor.AesCardEncryptor;
import com.example.bank_rest.util.card_masker.CardMasker;
import com.example.bank_rest.util.converter.DTOConverterFactory;
import com.example.bank_rest.util.converter.EntityConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final DTOConverterFactory converterFactory;
    private final CardMasker cardMasker;
    private final AesCardEncryptor aesCardEncryptor;

    public Card createCard(CardDTO dto) throws Exception {
        dto.setMaskedNumber(cardMasker.mask(dto.getCard_number()));
        dto.setCard_number(aesCardEncryptor.encrypt(dto.getCard_number()));
        dto.setStatus("ACTIVE");
        dto.setBalance(BigDecimal.valueOf(0.00));
        EntityConverter<Card, CardDTO> converter = converterFactory.getConverter(Card.class, CardDTO.class);

        dto.validate();
        return converter.toEntity(dto);
    }


}
