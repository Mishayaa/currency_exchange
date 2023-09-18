package org.example.dtos;


import org.example.entities.Currency;

import java.math.BigDecimal;

public record Exchange(
        Currency baseCurrency,
        Currency targetCurrency,
        BigDecimal rate,
        BigDecimal amount,
        BigDecimal convertedAmount
) {
}