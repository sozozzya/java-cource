package ru.senla.hotel.service.exception.guest;

public class GuestNotFoundException extends GuestException {
    public GuestNotFoundException(String name) {
        super("Guest " + name + " not found.");
    }
}
