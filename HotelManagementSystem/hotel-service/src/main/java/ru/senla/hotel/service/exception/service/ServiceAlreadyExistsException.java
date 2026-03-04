package ru.senla.hotel.service.exception.service;

public class ServiceAlreadyExistsException extends ServiceException {
    public ServiceAlreadyExistsException(String name) {
        super("Service '" + name + "' already exists.");
    }
}
