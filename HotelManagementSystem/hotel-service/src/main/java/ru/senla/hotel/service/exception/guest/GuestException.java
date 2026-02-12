package ru.senla.hotel.service.exception.guest;

import ru.senla.hotel.service.exception.ManagerException;

public class GuestException extends ManagerException {
    public GuestException(String message) {
        super(message);
    }

    public GuestException(String message, Throwable cause) {
        super(message, cause);
    }
}
