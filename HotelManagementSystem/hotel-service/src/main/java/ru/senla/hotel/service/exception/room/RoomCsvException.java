package ru.senla.hotel.service.exception.room;

import ru.senla.hotel.service.exception.service.ServiceException;

public class RoomCsvException extends ServiceException {
    public RoomCsvException(String message) {
        super(message);
    }
}
