package ru.senla.hotel.exception.guest;

import ru.senla.hotel.exception.ManagerException;

public class GuestException extends ManagerException {
    public GuestException(String message) {
        super(message);
    }

    public GuestException(String message, Throwable cause) {
        super(message, cause);
    }
}
