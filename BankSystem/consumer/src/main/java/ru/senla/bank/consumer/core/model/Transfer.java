package ru.senla.bank.consumer.core.model;

import ru.senla.bank.common.model.TransferStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record Transfer(
        UUID id,
        UUID fromAccountId,
        UUID toAccountId,
        BigDecimal amount,
        TransferStatus status
) {
}
