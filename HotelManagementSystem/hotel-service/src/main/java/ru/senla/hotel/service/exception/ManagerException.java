package ru.senla.hotel.service.exception;

<<<<<<< HEAD
import ru.senla.hotel.domain.exception.DomainException;

public class ManagerException extends DomainException {
=======
import ru.senla.hotel.model.exception.HotelException;

public class ManagerException extends HotelException {
>>>>>>> main
    public ManagerException(String message) {
        super(message);
    }

    public ManagerException(String message, Throwable cause) {
        super(message, cause);
    }
}
