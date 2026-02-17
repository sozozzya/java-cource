package ru.senla.hotel.service.exception;

import ru.senla.hotel.domain.exception.DomainException;

public class ManagerException extends DomainException {
    public ManagerException(String message) {
        super(message);
    }

    public ManagerException(String message, Throwable cause) {
        super(message, cause);
    }
}
