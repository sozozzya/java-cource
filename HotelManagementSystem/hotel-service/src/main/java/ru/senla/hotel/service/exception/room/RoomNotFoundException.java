package ru.senla.hotel.service.exception.room;

public class RoomNotFoundException extends RoomException {
    public RoomNotFoundException(int number) {
        super("Room " + number + " not found.");
    }
}
