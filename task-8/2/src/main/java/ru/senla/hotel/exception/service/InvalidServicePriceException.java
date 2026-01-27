package ru.senla.hotel.exception.service;

public class InvalidServicePriceException extends ServiceException {
    public InvalidServicePriceException(double price) {
        super("Invalid service price: " + price + ". Price must be non-negative.");
    }
}
