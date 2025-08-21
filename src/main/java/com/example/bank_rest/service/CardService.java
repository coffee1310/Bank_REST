package com.example.bank_rest.service;

import com.example.bank_rest.dto.CardDTO;
import com.example.bank_rest.entity.Card;
import com.example.bank_rest.exception.CardNotFoundException;
import com.example.bank_rest.exception.UserDoesNotExistException;
import com.example.bank_rest.repository.CardRepository;
import com.example.bank_rest.util.Encryptor.AesCardEncryptor;
import com.example.bank_rest.util.Encryptor.DeterministicEncryptor;
import com.example.bank_rest.util.card_masker.CardMasker;
import com.example.bank_rest.util.converter.DTOConverterFactory;
import com.example.bank_rest.util.converter.EntityConverter;
import jakarta.transaction.Transactional;
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
    private final DeterministicEncryptor deterministicEncryptor;
    private final Validator validator;

    private final CardRepository cardRepository;

    @Transactional
    public CardDTO createCard(CardDTO dto) throws UserDoesNotExistException {
        dto.setMaskedNumber(cardMasker.mask(dto.getCard_number()));
        dto.setStatus("ACTIVE");
        dto.setBalance(BigDecimal.ZERO);

        dto.validate(validator);

        String encryptedCardNumber = Try.of(() -> deterministicEncryptor.encrypt(dto.getCard_number())).get();
        if (cardRepository.existsCardByCardNumber(encryptedCardNumber)) {
            throw new IllegalArgumentException("Карта с таким номером уже существует");
        }

        dto.setCard_number(encryptedCardNumber);
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

    public Optional<CardDTO> getCard(Long id) throws CardNotFoundException{
        EntityConverter<Card, CardDTO> converter = converterFactory.getConverter(Card.class, CardDTO.class);

        Card card = cardRepository.getCardsById(id)
                .orElseThrow(() -> new CardNotFoundException("Card with this id was not found"));
        CardDTO cardDTO = converter.toDto(card);
        return Optional.of(cardDTO);
    }

    public Optional<CardDTO> setCardStatus(Long id, String status) throws CardNotFoundException {
        EntityConverter<Card, CardDTO> converter = converterFactory.getConverter(Card.class, CardDTO.class);

        Card card = cardRepository.setCardStatusById(status, id)
                .orElseThrow(() -> new CardNotFoundException("Card with this id was not found"));
        return Optional.of(converter.toDto(card));
    }
}
