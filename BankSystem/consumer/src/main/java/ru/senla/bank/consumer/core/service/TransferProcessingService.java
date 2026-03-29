package ru.senla.bank.consumer.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.senla.bank.common.model.TransferEvent;
import ru.senla.bank.common.model.TransferStatus;
import ru.senla.bank.consumer.core.exception.ValidationException;
import ru.senla.bank.consumer.core.model.Account;
import ru.senla.bank.consumer.core.model.Transfer;
import ru.senla.bank.consumer.core.ports.BankRepositoryPort;
import ru.senla.bank.consumer.core.ports.TransactionPort;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class TransferProcessingService {

    private static final Logger log = LoggerFactory.getLogger(TransferProcessingService.class);

    private final BankRepositoryPort repository;
    private final TransactionPort transaction;

    public TransferProcessingService(
            BankRepositoryPort repository,
            TransactionPort transaction
    ) {
        this.repository = Objects.requireNonNull(repository);
        this.transaction = Objects.requireNonNull(transaction);
    }

    public void process(TransferEvent event) {
        try {
            transaction.execute(() -> processInTransaction(event));
        } catch (ValidationException e) {
            log.warn("Validation failed: id={}, reason={}", event.id(), e.getMessage());
            saveFailed(event);
        } catch (Exception e) {
            log.error("Processing failed: id={}", event.id(), e);
            saveFailed(event);
        }
    }

    private void processInTransaction(TransferEvent event) {

        UUID firstId = event.fromAccountId().compareTo(event.toAccountId()) < 0
                ? event.fromAccountId()
                : event.toAccountId();

        UUID secondId = event.fromAccountId().compareTo(event.toAccountId()) < 0
                ? event.toAccountId()
                : event.fromAccountId();

        Account first = repository.findAccountByIdForUpdate(firstId)
                .orElseThrow(() -> new ValidationException("Account not found"));

        Account second = repository.findAccountByIdForUpdate(secondId)
                .orElseThrow(() -> new ValidationException("Account not found"));

        Account from = first.id().equals(event.fromAccountId()) ? first : second;
        Account to = first.id().equals(event.toAccountId()) ? first : second;

        validate(event, from);

        Account updatedFrom = new Account(
                from.id(),
                from.balance().subtract(event.amount())
        );

        Account updatedTo = new Account(
                to.id(),
                to.balance().add(event.amount())
        );

        repository.updateAccount(updatedFrom);
        repository.updateAccount(updatedTo);

        repository.saveTransfer(new Transfer(
                event.id(),
                event.fromAccountId(),
                event.toAccountId(),
                event.amount(),
                TransferStatus.SUCCESS
        ));

        log.info("Transfer SUCCESS: id={}", event.id());
    }

    private void validate(TransferEvent event, Account from) {
        if (event.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Amount must be positive");
        }

        if (from.balance().compareTo(event.amount()) < 0) {
            throw new ValidationException("Insufficient funds");
        }

        if (event.fromAccountId().equals(event.toAccountId())) {
            throw new ValidationException("Same account transfer");
        }
    }

    private void saveFailed(TransferEvent event) {
        try {
            repository.saveTransfer(new Transfer(
                    event.id(),
                    event.fromAccountId(),
                    event.toAccountId(),
                    event.amount(),
                    TransferStatus.FAILED
            ));
        } catch (Exception ex) {
            log.error("Failed to save FAILED transfer: id={}", event.id(), ex);
        }
    }
}
