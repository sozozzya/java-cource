package ru.senla.hotel.exception.room;

import ru.senla.hotel.exception.service.ServiceException;

public class RoomCsvException extends ServiceException {
    public RoomCsvException(String message) {
        super(message);
    }
}
