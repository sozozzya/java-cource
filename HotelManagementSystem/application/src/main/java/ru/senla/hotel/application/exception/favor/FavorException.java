package ru.senla.hotel.application.exception.favor;


import ru.senla.hotel.application.exception.ManagerException;

public class FavorException extends ManagerException {
    public FavorException(String message) {
        super(message);
    }

    public FavorException(String message, Throwable cause) {
        super(message, cause);
    }
}
