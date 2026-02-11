package ru.senla.hotel.service.exception;

import ru.senla.hotel.service.exception.room.RoomException;

public class FeatureDisabledException extends RoomException {
    public FeatureDisabledException(String message) {
        super(message);
    }
}
