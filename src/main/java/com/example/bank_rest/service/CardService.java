package com.example.bank_rest.service;

import com.example.bank_rest.dto.CardDTO;
import com.example.bank_rest.entity.Card;
import com.example.bank_rest.exception.UserDoesNotExistException;
import com.example.bank_rest.repository.CardRepository;
import com.example.bank_rest.util.Encryptor.AesCardEncryptor;
import com.example.bank_rest.util.card_masker.CardMasker;
import com.example.bank_rest.util.converter.DTOConverterFactory;
import com.example.bank_rest.util.converter.EntityConverter;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import io.vavr.control.Try;

@Service
@RequiredArgsConstructor
public class CardService {

    private final DTOConverterFactory converterFactory;
    private final CardMasker cardMasker;
    private final AesCardEncryptor aesCardEncryptor;
    private final Validator validator;

    private final CardRepository cardRepository;

    public CardDTO createCard(CardDTO dto) throws Exception {
        dto.setMaskedNumber(cardMasker.mask(dto.getCard_number()));
        dto.setCard_number(dto.getCard_number());
        dto.setStatus("ACTIVE");
        dto.setBalance(BigDecimal.valueOf(0.00));
        dto.setUser_id(dto.getUser_id());

        dto.validate(validator);
        dto.setCard_number(aesCardEncryptor.encrypt(dto.getCard_number()));
        EntityConverter<Card, CardDTO> converter = converterFactory.getConverter(Card.class, CardDTO.class);

        Card card = converter.toEntity(dto);
        cardRepository.save(card);
        return dto;
    }

    public List<CardDTO> getCards(String username) throws UserDoesNotExistException {
        EntityConverter<Card, CardDTO> converter = converterFactory.getConverter(Card.class, CardDTO.class);

        return cardRepository.getCardsByUser_Username(username).stream()
                .map(card -> Try.of(() -> converter.toDto(card))
                .toJavaOptional())
                .filter(Optional::isPresent)
                .map(Optional::get).toList();
    }
}
