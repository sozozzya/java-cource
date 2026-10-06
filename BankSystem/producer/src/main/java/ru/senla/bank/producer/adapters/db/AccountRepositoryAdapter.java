package ru.senla.bank.producer.adapters.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.senla.bank.producer.adapters.db.entity.AccountEntity;
import ru.senla.bank.producer.adapters.db.repository.AccountJpaRepository;
import ru.senla.bank.producer.core.model.Account;
import ru.senla.bank.producer.core.ports.AccountRepositoryPort;

import java.util.List;
import java.util.Objects;

public class AccountRepositoryAdapter implements AccountRepositoryPort {

    private static final Logger log = LoggerFactory.getLogger(AccountRepositoryAdapter.class);

    private final AccountJpaRepository repository;

    public AccountRepositoryAdapter(AccountJpaRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    public boolean isEmpty() {
        return repository.count() == 0;
    }

    @Override
    public List<Account> findAll() {
        try {
            return repository.findAll()
                    .stream()
                    .map(this::toDomain)
                    .toList();
        } catch (Exception e) {
            log.error("Failed to fetch accounts", e);
            throw e;
        }
    }

    @Override
    public void saveAll(List<Account> accounts) {
        try {
            List<AccountEntity> entities = accounts.stream()
                    .map(this::toEntity)
                    .toList();

            repository.saveAll(entities);
        } catch (Exception e) {
            log.error("Failed to save accounts", e);
            throw e;
        }
    }

    private Account toDomain(AccountEntity entity) {
        return new Account(
                entity.getId(),
                entity.getBalance()
        );
    }

    private AccountEntity toEntity(Account account) {
        return new AccountEntity(
                account.id(),
                account.balance()
        );
    }
}
