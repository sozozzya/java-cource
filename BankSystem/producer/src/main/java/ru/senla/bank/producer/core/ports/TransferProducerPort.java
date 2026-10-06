package ru.senla.bank.producer.core.ports;

import ru.senla.bank.common.model.TransferEvent;

public interface TransferProducerPort {

    void send(TransferEvent event);
}
