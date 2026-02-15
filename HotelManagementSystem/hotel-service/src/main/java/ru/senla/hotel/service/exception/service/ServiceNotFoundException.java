package ru.senla.hotel.service.exception.service;

public class ServiceNotFoundException extends ServiceException {
    public ServiceNotFoundException(String name) {
        super("Service " + name + " not found.");
    }
}
