package ru.senla.hotel.application.exception.booking;

public class RoomUnavailableException extends BookingException {
    public RoomUnavailableException(int roomNumber, String reason) {
        super("Room " + roomNumber + " is unavailable: " + reason);
    }
}
