package ru.senla.hotel.service.exception.booking;

public class BookingNotFoundException extends BookingException {
    public BookingNotFoundException(String message) {
        super(message);
    }
}
