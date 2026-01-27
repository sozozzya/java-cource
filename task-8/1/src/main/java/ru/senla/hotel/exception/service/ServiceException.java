package ru.senla.hotel.exception.service;

import ru.senla.hotel.exception.ManagerException;

public abstract class ServiceException extends ManagerException {
    public ServiceException(String message) {
        super(message);
    }
}
