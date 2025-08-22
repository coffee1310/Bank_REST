package com.example.bank_rest.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "transfers")
public class Transfer {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_from", nullable = false)
    @ToString.Exclude
    private Card card_from;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_to", nullable = false)
    @ToString.Exclude
    private Card card_to;

    @Column(name = "amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "created_at")
    LocalDateTime created_at;
}
