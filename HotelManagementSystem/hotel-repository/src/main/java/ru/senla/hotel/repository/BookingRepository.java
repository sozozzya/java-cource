package ru.senla.hotel.repository;

import ru.senla.hotel.domain.model.Booking;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends GenericRepository<Booking, Long> {

    List<Booking> findActiveByDate(LocalDate date);

    List<Booking> findByGuestId(Long guestId);

    List<Booking> findBookingsByRoomAndPeriod(Long roomId, LocalDate checkIn, LocalDate checkOut);

    List<Booking> findAllWithRelations();

    List<Booking> findCompletedByRoomId(Long roomId);

    Optional<Booking> findActiveByRoomNumber(int roomNumber, LocalDate date);

    boolean existsFutureBookings(Long roomId, LocalDate fromDate);
}
