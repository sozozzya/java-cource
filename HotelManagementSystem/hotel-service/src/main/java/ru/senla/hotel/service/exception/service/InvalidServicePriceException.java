package ru.senla.hotel.service.exception.service;

import java.math.BigDecimal;

public class InvalidServicePriceException extends ServiceException {
    public InvalidServicePriceException(BigDecimal price) {
        super("Invalid service price: " + price + ". Price must be non-negative.");
    }
}
