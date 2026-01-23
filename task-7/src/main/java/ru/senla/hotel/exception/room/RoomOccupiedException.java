package ru.senla.hotel.exception.room;

public class RoomOccupiedException extends RoomException {
    public RoomOccupiedException(int number) {
        super("Room " + number + " is currently occupied.");
    }
}
