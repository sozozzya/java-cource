package ru.senla.hotel.dao.exception;

public class DAOException extends RuntimeException {
    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }
}
