package com.example.bank_rest.service;

import com.example.bank_rest.dto.CardStatusRequestDTO;
import com.example.bank_rest.entity.CardStatusRequest;
import com.example.bank_rest.repository.CardStatusRequestRepository;
import com.example.bank_rest.util.converter.DTOConverterFactory;
import com.example.bank_rest.util.converter.EntityConverter;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardStatusRequestService {

    private final CardStatusRequestRepository cardStatusRequestRepository;
    private final DTOConverterFactory converterFactory;
    private final Validator validator;

    public CardStatusRequest createCardStatusRequest(CardStatusRequestDTO cardStatusRequestDTO) {
        CardStatusRequest cardStatusRequest = getConverterFactory().toEntity(cardStatusRequestDTO);

        cardStatusRequestRepository.save(cardStatusRequest);
        return cardStatusRequest;
    }

    private EntityConverter<CardStatusRequest, CardStatusRequestDTO> getConverterFactory(){
        return converterFactory.getConverter(CardStatusRequest.class, CardStatusRequestDTO.class);
    }
}
