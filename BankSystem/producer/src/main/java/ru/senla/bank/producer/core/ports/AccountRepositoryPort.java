package ru.senla.bank.producer.core.ports;

import ru.senla.bank.producer.core.model.Account;

import java.util.List;

public interface AccountRepositoryPort {

    boolean isEmpty();

    List<Account> findAll();

    void saveAll(List<Account> accounts);
}
