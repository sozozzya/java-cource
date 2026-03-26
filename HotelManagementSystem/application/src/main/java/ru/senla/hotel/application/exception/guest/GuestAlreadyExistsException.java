package ru.senla.hotel.application.exception.guest;

public class GuestAlreadyExistsException extends GuestException {
    public GuestAlreadyExistsException(String name) {
        super("Guest " + name + " already exists.");
    }
}
