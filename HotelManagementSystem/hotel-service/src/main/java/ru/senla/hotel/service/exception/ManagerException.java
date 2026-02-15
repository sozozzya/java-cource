package ru.senla.hotel.service.exception;

import ru.senla.hotel.model.exception.HotelException;

public class ManagerException extends HotelException {
    public ManagerException(String message) {
        super(message);
    }

    public ManagerException(String message, Throwable cause) {
        super(message, cause);
    }
}
