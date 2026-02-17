package ru.senla.hotel.repository.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import ru.senla.hotel.repository.BookingRepository;
import ru.senla.hotel.repository.exception.RepositoryException;
import ru.senla.hotel.domain.model.Booking;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaBookingRepository extends JpaGenericRepository<Booking, Long> implements BookingRepository {

    private static final Logger log = LoggerFactory.getLogger(JpaBookingRepository.class);

    public JpaBookingRepository() {
        super(Booking.class);
    }

    @Override
    public List<Booking> findActiveByDate(LocalDate date) {
        log.debug("Attempting to find active bookings for date: {}", date);
        try {
            List<Booking> result = em.createQuery("""
                            SELECT b FROM Booking b
                            JOIN FETCH b.guest
                            JOIN FETCH b.room
                            WHERE b.checkInDate <= :date
                              AND b.checkOutDate >= :date
                            """, Booking.class)
                    .setParameter("date", date)
                    .getResultList();

            log.debug("Active bookings found: {}", result.size());
            return result;
        } catch (DataAccessException e) {
            log.error("Database error while finding active bookings for date: {}", date, e);
            throw new RepositoryException("Failed to find active bookings for date=" + date, e);
        } catch (Exception e) {
            log.error("Unexpected error while finding active bookings for date: {}", date, e);
            throw new RepositoryException("Unexpected error during active bookings search", e);
        }
    }

    @Override
    public List<Booking> findByGuestId(Long guestId) {
        log.debug("Attempting to find bookings by guestId: {}", guestId);
        try {
            List<Booking> result = em.createQuery("""
                            SELECT DISTINCT b FROM Booking b
                            JOIN FETCH b.room
                            LEFT JOIN FETCH b.services bs
                            LEFT JOIN FETCH bs.service
                            WHERE b.guest.id = :guestId
                            """, Booking.class)
                    .setParameter("guestId", guestId)
                    .getResultList();

            log.debug("Bookings found for guest {}: {}", guestId, result.size());
            return result;
        } catch (DataAccessException e) {
            log.error("Database error while finding bookings by guestId: {}", guestId, e);
            throw new RepositoryException("Failed to find bookings by guestId=" + guestId, e);
        } catch (Exception e) {
            log.error("Unexpected error while finding bookings by guestId: {}", guestId, e);
            throw new RepositoryException("Unexpected error during booking search by guestId", e);
        }
    }

    @Override
    public List<Booking> findAllWithRelations() {
        log.debug("Attempting to find all bookings with relations");
        try {
            List<Booking> result = em.createQuery("""
                            SELECT DISTINCT b FROM Booking b
                            LEFT JOIN FETCH b.guest
                            LEFT JOIN FETCH b.room
                            LEFT JOIN FETCH b.services
                            """, Booking.class)
                    .getResultList();

            log.debug("Total bookings fetched with relations: {}", result.size());
            return result;
        } catch (DataAccessException e) {
            log.error("Database error while fetching all bookings with relations", e);
            throw new RepositoryException("Failed to fetch all bookings with relations", e);
        } catch (Exception e) {
            log.error("Unexpected error while fetching all bookings with relations", e);
            throw new RepositoryException("Unexpected error during fetching bookings", e);
        }
    }

    @Override
    public List<Booking> findCompletedByRoomId(Long roomId) {
        log.debug("Attempting to find completed bookings for roomId: {}", roomId);
        try {
            List<Booking> result = em.createQuery("""
                            SELECT b FROM Booking b
                            JOIN FETCH b.guest
                            WHERE b.room.id = :roomId
                              AND b.checkOutDate <= :today
                            ORDER BY b.checkOutDate DESC
                            """, Booking.class)
                    .setParameter("roomId", roomId)
                    .setParameter("today", LocalDate.now())
                    .getResultList();

            log.debug("Completed bookings found for room {}: {}", roomId, result.size());
            return result;
        } catch (DataAccessException e) {
            log.error("Database error while finding completed bookings for room: {}", roomId, e);
            throw new RepositoryException("Failed to find completed bookings for roomId=" + roomId, e);
        } catch (Exception e) {
            log.error("Unexpected error while finding completed bookings for room: {}", roomId, e);
            throw new RepositoryException("Unexpected error during completed bookings search", e);
        }
    }

    @Override
    public List<Booking> findBookingsByRoomAndPeriod(Long roomId, LocalDate checkIn, LocalDate checkOut) {
        log.debug("Checking overlapping bookings for room {} between {} and {}", roomId, checkIn, checkOut);
        try {
            List<Booking> result = em.createQuery("""
                            SELECT b FROM Booking b
                            WHERE b.room.id = :roomId
                              AND b.checkOutDate > :checkIn
                              AND b.checkInDate < :checkOut
                            """, Booking.class)
                    .setParameter("roomId", roomId)
                    .setParameter("checkIn", checkIn)
                    .setParameter("checkOut", checkOut)
                    .getResultList();

            log.debug("Overlapping bookings found: {}", result.size());
            return result;
        } catch (DataAccessException e) {
            log.error("Database error while checking bookings for room {}", roomId, e);
            throw new RepositoryException("Failed to check bookings for roomId=" + roomId, e);
        } catch (Exception e) {
            log.error("Unexpected error while checking bookings for room {}", roomId, e);
            throw new RepositoryException("Unexpected error during booking period check", e);
        }
    }

    @Override
    public Optional<Booking> findActiveByRoomNumber(int roomNumber, LocalDate date) {
        log.debug("Attempting to find active booking for roomNumber {} on date {}", roomNumber, date);
        try {
            Optional<Booking> result = em.createQuery("""
                            SELECT b FROM Booking b
                            JOIN b.room r
                            WHERE r.number = :roomNumber
                              AND b.checkInDate <= :date
                              AND b.checkOutDate >= :date
                            """, Booking.class)
                    .setParameter("roomNumber", roomNumber)
                    .setParameter("date", date)
                    .getResultStream()
                    .findFirst();

            log.debug("Active booking found: {}", result.isPresent());
            return result;
        } catch (DataAccessException e) {
            log.error("Database error while finding active booking for roomNumber {}", roomNumber, e);
            throw new RepositoryException("Failed to find active booking for roomNumber=" + roomNumber, e);
        } catch (Exception e) {
            log.error("Unexpected error while finding active booking for roomNumber {}", roomNumber, e);
            throw new RepositoryException("Unexpected error during active booking search", e);
        }
    }

    @Override
    public boolean existsFutureBookings(Long roomId, LocalDate fromDate) {
        log.debug("Checking future bookings for room {} from date {}", roomId, fromDate);
        try {
            Long count = em.createQuery("""
                            SELECT COUNT(b) FROM Booking b
                            WHERE b.room.id = :roomId
                              AND b.checkInDate >= :fromDate
                            """, Long.class)
                    .setParameter("roomId", roomId)
                    .setParameter("fromDate", fromDate)
                    .getSingleResult();

            boolean exists = count > 0;
            log.debug("Future bookings exist: {}", exists);
            return exists;
        } catch (DataAccessException e) {
            log.error("Database error while checking future bookings for room {}", roomId, e);
            throw new RepositoryException("Failed to check future bookings for roomId=" + roomId, e);
        } catch (Exception e) {
            log.error("Unexpected error while checking future bookings for room {}", roomId, e);
            throw new RepositoryException("Unexpected error during future bookings check", e);
        }
    }
}
