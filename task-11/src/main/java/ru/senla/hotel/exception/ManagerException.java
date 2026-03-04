package ru.senla.hotel.exception;

public class ManagerException extends HotelException {
    public ManagerException(String message) {
        super(message);
    }

    public ManagerException(String message, Throwable cause) {
        super(message, cause);
    }
}
