package ru.senla.bank.producer.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.senla.bank.producer.core.model.Account;
import ru.senla.bank.producer.core.ports.AccountRepositoryPort;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AccountInitializationService {

    private static final Logger log = LoggerFactory.getLogger(AccountInitializationService.class);
    private static final int ACCOUNTS_COUNT = 1000;

    private final AccountRepositoryPort repository;
    private final Map<UUID, Account> accountsCache = new ConcurrentHashMap<>();
    private final SecureRandom random = new SecureRandom();

    public AccountInitializationService(AccountRepositoryPort repository) {
        this.repository = repository;
    }

    public void init() {
        if (repository.isEmpty()) {
            log.info("Accounts table is empty. Generating {} accounts...", ACCOUNTS_COUNT);

            List<Account> accounts = generateAccounts();
            repository.saveAll(accounts);

            accounts.forEach(acc -> accountsCache.put(acc.id(), acc));

            log.info("Generated and saved {} accounts", accounts.size());
        } else {
            log.info("Loading accounts from DB...");

            List<Account> accounts = repository.findAll();
            accounts.forEach(acc -> accountsCache.put(acc.id(), acc));

            log.info("Loaded {} accounts into cache", accounts.size());
        }
    }

    public Collection<Account> getAllAccounts() {
        return accountsCache.values();
    }

    private List<Account> generateAccounts() {
        List<Account> accounts = new ArrayList<>(ACCOUNTS_COUNT);

        for (int i = 0; i < ACCOUNTS_COUNT; i++) {
            accounts.add(new Account(
                    UUID.randomUUID(),
                    randomBalance()
            ));
        }

        return accounts;
    }

    private BigDecimal randomBalance() {
        return BigDecimal.valueOf(1_000 + random.nextInt(9_000));
    }
}
