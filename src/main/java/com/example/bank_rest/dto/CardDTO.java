package com.example.bank_rest.dto;

import com.example.bank_rest.util.annotation.UserExists;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@RequiredArgsConstructor
public class CardDTO implements Validatable {

    private Long id;

    private String card_number;

    private Date expiry_date;

    @UserExists
    private Long user_id;

    @Override
    public void validate() {

    }
}
