package ru.senla.bank.producer.adapters.scheduler;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import ru.senla.bank.producer.core.service.AccountInitializationService;
import ru.senla.bank.producer.core.service.TransferGenerationService;

import java.util.concurrent.atomic.AtomicBoolean;

public class TransferScheduler {

    private static final Logger log = LoggerFactory.getLogger(TransferScheduler.class);

    private static final int EVENTS_PER_SECOND = 5;

    private final TransferGenerationService generationService;
    private final AccountInitializationService accountService;

    private final AtomicBoolean running = new AtomicBoolean(false);

    public TransferScheduler(
            TransferGenerationService generationService,
            AccountInitializationService accountService
    ) {
        this.generationService = generationService;
        this.accountService = accountService;
    }

    @PostConstruct
    public void init() {
        accountService.init();
    }

    @Scheduled(fixedDelay = 1000)
    public void generate() {
        if (!running.compareAndSet(false, true)) {
            log.warn("Previous generation still running, skipping...");
            return;
        }

        try {
            generationService.generateAndSendBatch(EVENTS_PER_SECOND);
        } catch (Exception e) {
            log.error("Error during transfer generation", e);
        } finally {
            running.set(false);
        }
    }
}
