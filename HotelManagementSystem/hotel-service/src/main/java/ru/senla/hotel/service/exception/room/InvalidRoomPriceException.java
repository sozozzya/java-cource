package ru.senla.hotel.service.exception.room;

import java.math.BigDecimal;

public class InvalidRoomPriceException extends RoomException {
    public InvalidRoomPriceException(BigDecimal price) {
        super("Invalid room price: " + price + ". Price must be non-negative.");
    }
}
