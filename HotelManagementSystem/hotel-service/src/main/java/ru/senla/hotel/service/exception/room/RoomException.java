package ru.senla.hotel.service.exception.room;

import ru.senla.hotel.service.exception.ManagerException;

public class RoomException extends ManagerException {
    public RoomException(String message) {
        super(message);
    }

    public RoomException(String message, Throwable cause) {
        super(message, cause);
    }
}
