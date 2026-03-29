package ru.senla.bank.common.model;

import java.math.BigDecimal;
import java.util.UUID;

public record TransferEvent(
        UUID id,
        UUID fromAccountId,
        UUID toAccountId,
        BigDecimal amount
) {
}
