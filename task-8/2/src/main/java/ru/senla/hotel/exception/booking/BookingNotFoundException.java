package ru.senla.hotel.exception.booking;

public class BookingNotFoundException extends BookingException {
    public BookingNotFoundException(String message) {
        super(message);
    }
}
