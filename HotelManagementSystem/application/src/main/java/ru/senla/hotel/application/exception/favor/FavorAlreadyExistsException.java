package ru.senla.hotel.application.exception.favor;

public class FavorAlreadyExistsException extends FavorException {
    public FavorAlreadyExistsException(String name) {
        super("Service '" + name + "' already exists.");
    }
}
