package ru.senla.bank.consumer.core.ports;

import ru.senla.bank.consumer.core.model.Account;
import ru.senla.bank.consumer.core.model.Transfer;

import java.util.Optional;
import java.util.UUID;

public interface BankRepositoryPort {

    Optional<Account> findAccountById(UUID id);

    Optional<Account> findAccountByIdForUpdate(UUID id);

    void updateAccount(Account account);

    void saveTransfer(Transfer transfer);
}
