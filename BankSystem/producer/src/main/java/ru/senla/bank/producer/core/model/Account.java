package ru.senla.bank.producer.core.model;

import java.math.BigDecimal;
import java.util.UUID;

public record Account(
        UUID id,
        BigDecimal balance
) {
}
