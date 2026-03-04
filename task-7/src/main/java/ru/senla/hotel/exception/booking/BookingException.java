package ru.senla.hotel.exception.booking;

import ru.senla.hotel.exception.ManagerException;

public abstract class BookingException extends ManagerException {
    public BookingException(String message) {
        super(message);
    }
}
