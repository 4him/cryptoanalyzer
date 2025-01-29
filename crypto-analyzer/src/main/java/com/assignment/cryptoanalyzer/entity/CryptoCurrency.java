package com.assignment.cryptoanalyzer.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "crypto_currency")
public class CryptoCurrency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    private Long id;

    @Column (name = "timestamp")
    private Long timestamp;

    @Column (name = "currency_code")
    private String currencyCode;

    @Column (name = "price")
    private BigDecimal price;

}
