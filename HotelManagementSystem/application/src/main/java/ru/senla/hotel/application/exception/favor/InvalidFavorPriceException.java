package ru.senla.hotel.application.exception.favor;

import java.math.BigDecimal;

public class InvalidFavorPriceException extends FavorException {
    public InvalidFavorPriceException(BigDecimal price) {
        super("Invalid service price: " + price + ". Price must be non-negative.");
    }
}
