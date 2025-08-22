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
        card.setUser(getUser(dto.getUser_id()));
        card.setBalance(dto.getBalance());
        card.setExpiryDate(dto.getExpiry_date());
        card.setMaskedNumber(dto.getMaskedNumber());

        return card;
    }

    @Override
    public CardDTO toDto(Card entity) throws UserDoesNotExistException {
        CardDTO cardDTO = new CardDTO();
        cardDTO.setId(entity.getId());
        cardDTO.setCard_number(entity.getCardNumber());
        cardDTO.setBalance(entity.getBalance());
        cardDTO.setExpiry_date(entity.getExpiryDate());
        cardDTO.setMaskedNumber(entity.getMaskedNumber());
        cardDTO.setStatus(entity.getStatus());
        cardDTO.setUser_id(entity.getUser().getId());

        return cardDTO;
    }

    private User getUser(Long id) throws UserDoesNotExistException {
        return userRepository.findUsersById(id).orElseThrow(() -> new UserDoesNotExistException("User with this id doesn't exist"));
    }
}
