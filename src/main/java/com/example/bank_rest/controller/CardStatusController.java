package com.example.bank_rest.controller;

import com.example.bank_rest.dto.CardStatusRequestDTO;
import com.example.bank_rest.entity.CardStatusRequest;
import com.example.bank_rest.service.CardStatusRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
@RequestMapping("/api/card_status/")
public class CardStatusController {

    private final CardStatusRequestService cardStatusRequestService;

    @PostMapping
    public ResponseEntity<?> createRequest(@RequestBody @Valid CardStatusRequestDTO cardStatusRequestDTO) {
        CardStatusRequest cardStatusRequest = cardStatusRequestService.createCardStatusRequest(cardStatusRequestDTO);
        return new ResponseEntity<>(cardStatusRequest, HttpStatus.CREATED);
    }
}
