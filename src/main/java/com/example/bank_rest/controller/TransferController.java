package com.example.bank_rest.controller;

import com.example.bank_rest.dto.TransferDTO;
import com.example.bank_rest.entity.Transfer;
import com.example.bank_rest.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transfer")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<?> createTransfer(@RequestBody TransferDTO transferDTO) {
        Transfer transfer = transferService.transferMoney(transferDTO);
        return new ResponseEntity<>(transfer, HttpStatus.CREATED);
    }
}
