package ru.senla.hotel.application.exception.favor;

public class FavorNotFoundException extends FavorException {
    public FavorNotFoundException(String name) {
        super("Service " + name + " not found.");
    }
}
