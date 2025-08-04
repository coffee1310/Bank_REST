package com.example.bank_rest.util.card_masker;

import org.springframework.stereotype.Component;

@Component
public class CardMasker implements IMasker {
    @Override
    public String mask(String card_number) {
        return "**** **** **** " + card_number.substring(12);
    }
}
