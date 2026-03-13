package ru.senla.hotel.application.exception.room;


import ru.senla.hotel.application.exception.ManagerException;

public class RoomException extends ManagerException {
    public RoomException(String message) {
        super(message);
    }

    public RoomException(String message, Throwable cause) {
        super(message, cause);
    }
}
