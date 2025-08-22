package com.example.bank_rest.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "transfer")
public class Transfer {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_from")
    @ToString.Exclude
    Card card_from;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_to")
    @ToString.Exclude
    Card card_to;

    @Column(name = "amount")
    BigDecimal amount;
}
