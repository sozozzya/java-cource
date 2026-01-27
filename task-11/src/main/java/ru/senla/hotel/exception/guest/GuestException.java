package ru.senla.hotel.exception.guest;

import ru.senla.hotel.exception.ManagerException;

public abstract class GuestException extends ManagerException {
    public GuestException(String message) {
        super(message);
    }
}
