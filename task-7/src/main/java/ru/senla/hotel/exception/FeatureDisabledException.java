package ru.senla.hotel.exception;

import ru.senla.hotel.exception.room.RoomException;

public class FeatureDisabledException extends RoomException {
    public FeatureDisabledException(String message) {
        super(message);
    }
}
