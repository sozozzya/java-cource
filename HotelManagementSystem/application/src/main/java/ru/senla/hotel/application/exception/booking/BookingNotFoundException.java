package ru.senla.hotel.application.exception.booking;

public class BookingNotFoundException extends BookingException {
    public BookingNotFoundException(String message) {
        super(message);
    }
}
