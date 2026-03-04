package ru.senla.hotel.exception.booking;

import ru.senla.hotel.exception.ManagerException;

public class BookingException extends ManagerException {
    public BookingException(String message) {
        super(message);
    }

    public BookingException(String message, Throwable cause) {
        super(message, cause);
    }
}
