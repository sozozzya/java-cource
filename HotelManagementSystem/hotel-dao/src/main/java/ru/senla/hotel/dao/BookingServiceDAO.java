package ru.senla.hotel.dao;

import ru.senla.hotel.dao.exception.DAOException;
import ru.senla.hotel.model.BookingService;

import java.util.List;

public interface BookingServiceDAO extends GenericDAO<BookingService, Long> {

    void removeServiceFromBooking(Long bookingId, Long serviceId) throws DAOException;

    List<Long> findServiceIdsByBooking(Long bookingId) throws DAOException;

    List<BookingService> findServicesByGuestName(String guestName) throws DAOException;
}
