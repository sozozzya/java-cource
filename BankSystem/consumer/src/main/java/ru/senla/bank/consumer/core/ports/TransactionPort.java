package ru.senla.bank.consumer.core.ports;

public interface TransactionPort {

    void execute(Runnable action);
}
