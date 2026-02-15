package ru.senla.hotel.service.exception.service;

import ru.senla.hotel.service.exception.ManagerException;

public class ServiceException extends ManagerException {
    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
