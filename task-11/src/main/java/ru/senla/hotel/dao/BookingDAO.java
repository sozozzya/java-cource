package ru.senla.hotel.dao;

import ru.senla.hotel.exception.DAOException;
import ru.senla.hotel.model.Booking;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingDAO extends GenericDAO<Booking, Long> {

    List<Booking> findActiveByDate(LocalDate date) throws DAOException;

    List<Booking> findByGuestId(Long guestId) throws DAOException;

    Optional<Booking> findActiveByRoomId(Long roomId, LocalDate date) throws DAOException;

    void addServiceToBooking(Long bookingId, Long serviceId) throws DAOException;

    List<Long> findServiceIdsByBooking(Long bookingId) throws DAOException;
}
