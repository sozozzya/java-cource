package ru.senla.bank.producer.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.senla.bank.common.model.TransferEvent;
import ru.senla.bank.producer.core.model.Account;
import ru.senla.bank.producer.core.ports.TransferProducerPort;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TransferGenerationService {

    private static final Logger log = LoggerFactory.getLogger(TransferGenerationService.class);

    private final TransferProducerPort producer;
    private final AccountInitializationService accountService;
    private final SecureRandom random = new SecureRandom();

    public TransferGenerationService(
            TransferProducerPort producer,
            AccountInitializationService accountService
    ) {
        this.producer = producer;
        this.accountService = accountService;
    }

    public void generateAndSendBatch(int batchSize) {
        List<Account> accounts = new ArrayList<>(accountService.getAllAccounts());

        for (int i = 0; i < batchSize; i++) {
            TransferEvent event = randomTransfer(accounts);

            producer.send(event);

            log.info("Transfer event sent: id={}, from={}, to={}, amount={}",
                    event.id(),
                    event.fromAccountId(),
                    event.toAccountId(),
                    event.amount()
            );
        }
    }

    private TransferEvent randomTransfer(List<Account> accounts) {
        Account from;
        Account to;

        do {
            from = accounts.get(random.nextInt(accounts.size()));
            to = accounts.get(random.nextInt(accounts.size()));
        } while (from.id().equals(to.id()));

        BigDecimal maxAmount = from.balance().min(BigDecimal.valueOf(1000));

        if (maxAmount.compareTo(BigDecimal.ONE) < 0) {
            maxAmount = BigDecimal.ONE;
        }

        BigDecimal amount = BigDecimal.valueOf(
                1 + random.nextInt(maxAmount.intValue())
        );

        return new TransferEvent(
                UUID.randomUUID(),
                from.id(),
                to.id(),
                amount
        );
    }

    private BigDecimal randomAmount() {
        return BigDecimal.valueOf(1 + random.nextInt(1000));
    }
}
