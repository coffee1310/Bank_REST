package com.example.bank_rest.util.converter;

import com.example.bank_rest.dto.CardDTO;
import com.example.bank_rest.entity.Card;
import com.example.bank_rest.entity.User;
import com.example.bank_rest.exception.UserDoesNotExistException;
import com.example.bank_rest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CardConverter implements EntityConverter<Card, CardDTO> {

    private final UserRepository userRepository;

    @Override
    public Card toEntity(CardDTO dto) throws UserDoesNotExistException {
        Card card = new Card();
        card.setId(dto.getId());
        card.setCardNumber(dto.getCard_number());
        card.setStatus(dto.getStatus());
        card.setUser(getUser(dto.getId()));
        card.setBalance(dto.getBalance());
        card.setExpiryDate(dto.getExpiry_date());
        card.setMaskedNumber(dto.getMaskedNumber());

        return card;
    }

    private User getUser(Long id) throws UserDoesNotExistException {
        return userRepository.findUsersById(id).orElseThrow(() -> new UserDoesNotExistException(""));
    }
}
