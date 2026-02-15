package ru.senla.hotel.dao;

import ru.senla.hotel.dao.exception.DAOException;
import ru.senla.hotel.model.Booking;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingDAO extends GenericDAO<Booking, Long> {

    List<Booking> findActiveByDate(LocalDate date) throws DAOException;

    List<Booking> findByGuestId(Long guestId) throws DAOException;

    List<Booking> findBookingsByRoomAndPeriod(Long roomId, LocalDate checkIn, LocalDate checkOut) throws DAOException;

    List<Booking> findAllWithRelations() throws DAOException;

    List<Booking> findCompletedByRoomId(Long roomId) throws DAOException;

    Optional<Booking> findActiveByRoomId(Long roomId, LocalDate date) throws DAOException;

    Optional<Booking> findActiveByRoomNumber(int roomNumber, LocalDate date) throws DAOException;

    boolean existsFutureBookings(Long roomId, LocalDate fromDate) throws DAOException;
}
