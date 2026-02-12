package ru.senla.hotel.service.exception.room;

public class InvalidRoomPriceException extends RoomException {
    public InvalidRoomPriceException(double price) {
        super("Invalid room price: " + price + ". Price must be non-negative.");
    }
}
