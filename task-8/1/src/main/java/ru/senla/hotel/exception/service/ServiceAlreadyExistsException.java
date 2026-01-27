package ru.senla.hotel.exception.service;

public class ServiceAlreadyExistsException extends ServiceException {
    public ServiceAlreadyExistsException(String name) {
        super("Service '" + name + "' already exists.");
    }
}
