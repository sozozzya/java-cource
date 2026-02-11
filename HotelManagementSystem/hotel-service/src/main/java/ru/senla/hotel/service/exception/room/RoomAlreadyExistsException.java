package ru.senla.hotel.service.exception.room;

public class RoomAlreadyExistsException extends RoomException {
    public RoomAlreadyExistsException(int number) {
        super("Room " + number + " already exists.");
    }
}
