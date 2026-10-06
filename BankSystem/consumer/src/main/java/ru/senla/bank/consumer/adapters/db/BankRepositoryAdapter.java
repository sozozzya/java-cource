package ru.senla.bank.consumer.adapters.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.senla.bank.consumer.adapters.db.entity.AccountEntity;
import ru.senla.bank.consumer.adapters.db.entity.TransferEntity;
import ru.senla.bank.consumer.adapters.db.repository.AccountJpaRepository;
import ru.senla.bank.consumer.adapters.db.repository.TransferJpaRepository;
import ru.senla.bank.consumer.core.model.Account;
import ru.senla.bank.consumer.core.model.Transfer;
import ru.senla.bank.consumer.core.ports.BankRepositoryPort;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class BankRepositoryAdapter implements BankRepositoryPort {

    private static final Logger log = LoggerFactory.getLogger(BankRepositoryAdapter.class);

    private final AccountJpaRepository accountRepository;
    private final TransferJpaRepository transferRepository;

    public BankRepositoryAdapter(
            AccountJpaRepository accountRepository,
            TransferJpaRepository transferRepository
    ) {
        this.accountRepository = Objects.requireNonNull(accountRepository);
        this.transferRepository = Objects.requireNonNull(transferRepository);
    }

    @Override
    public Optional<Account> findAccountById(UUID id) {
        try {
            return accountRepository.findById(id)
                    .map(this::toDomain);
        } catch (Exception e) {
            log.error("Failed to find account: id={}", id, e);
            throw e;
        }
    }

    @Override
    public Optional<Account> findAccountByIdForUpdate(UUID id) {
        try {
            return accountRepository.findByIdForUpdate(id)
                    .map(this::toDomain);
        } catch (Exception e) {
            log.error("Failed to lock account: id={}", id, e);
            throw e;
        }
    }

    @Override
    public void updateAccount(Account account) {
        try {
            AccountEntity entity = accountRepository.findById(account.id())
                    .orElseThrow(() -> new RuntimeException("Account not found"));

            entity.setBalance(account.balance());
            accountRepository.save(entity);

        } catch (Exception e) {
            log.error("Failed to update account: id={}", account.id(), e);
            throw e;
        }
    }

    @Override
    public void saveTransfer(Transfer transfer) {
        try {
            transferRepository.save(toEntity(transfer));
        } catch (Exception e) {
            log.error("Failed to save transfer: id={}", transfer.id(), e);
            throw e;
        }
    }

    private Account toDomain(AccountEntity entity) {
        return new Account(
                entity.getId(),
                entity.getBalance()
        );
    }

    private TransferEntity toEntity(Transfer transfer) {
        return new TransferEntity(
                transfer.id(),
                transfer.fromAccountId(),
                transfer.toAccountId(),
                transfer.amount(),
                transfer.status()
        );
    }
}
