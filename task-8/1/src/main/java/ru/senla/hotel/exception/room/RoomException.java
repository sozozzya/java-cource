package ru.senla.hotel.exception.room;

import ru.senla.hotel.exception.ManagerException;

public abstract class RoomException extends ManagerException {
    public RoomException(String message) {
        super(message);
    }
}
