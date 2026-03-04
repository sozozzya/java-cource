package ru.senla.hotel.dao.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.senla.hotel.dao.BookingDAO;
import ru.senla.hotel.dao.exception.DAOException;
import ru.senla.hotel.db.JpaUtil;
import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.model.Booking;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class JpaBookingDAO extends JpaGenericDAO<Booking, Long> implements BookingDAO {

    private static final Logger log = LoggerFactory.getLogger(JpaBookingDAO.class);

    public JpaBookingDAO() {
        super(Booking.class);
    }

    @Override
    public List<Booking> findActiveByDate(LocalDate date) throws DAOException {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            return em.createQuery("""
                            SELECT b FROM Booking b
                            JOIN FETCH b.guest
                            JOIN FETCH b.room
                            WHERE b.checkInDate <= :date
                              AND b.checkOutDate >= :date
                            """, Booking.class)
                    .setParameter("date", date)
                    .getResultList();
        } catch (Exception e) {
            log.error("Failed to find active bookings for date {}", date, e);
            throw new DAOException("Failed to find active bookings", e);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Booking> findByGuestId(Long guestId) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT DISTINCT b FROM Booking b " +
                                    "JOIN FETCH b.room " +
                                    "LEFT JOIN FETCH b.services bs " +
                                    "LEFT JOIN FETCH bs.service " +
                                    "WHERE b.guest.id = :guestId",
                            Booking.class
                    )
                    .setParameter("guestId", guestId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Booking> findActiveByRoomId(Long roomId, LocalDate date) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            TypedQuery<Booking> query = em.createQuery(
                    "SELECT b FROM Booking b " +
                            "WHERE b.room.id = :roomId " +
                            "AND b.checkInDate <= :date " +
                            "AND b.checkOutDate >= :date",
                    Booking.class
            );
            query.setParameter("roomId", roomId);
            query.setParameter("date", date);

            return query.getResultStream().findFirst();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Booking> findAllWithRelations() {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            TypedQuery<Booking> query = em.createQuery(
                    "SELECT DISTINCT b FROM Booking b " +
                            "LEFT JOIN FETCH b.guest " +
                            "LEFT JOIN FETCH b.room " +
                            "LEFT JOIN FETCH b.services",
                    Booking.class
            );
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Booking> findCompletedByRoomId(Long roomId) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            TypedQuery<Booking> query = em.createQuery(
                    "SELECT b FROM Booking b " +
                            "JOIN FETCH b.guest " +
                            "WHERE b.room.id = :roomId " +
                            "AND b.checkOutDate <= :today " +
                            "ORDER BY b.checkOutDate DESC",
                    Booking.class
            );
            query.setParameter("roomId", roomId);
            query.setParameter("today", LocalDate.now());

            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Booking> findBookingsByRoomAndPeriod(Long roomId, LocalDate checkIn, LocalDate checkOut) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            TypedQuery<Booking> query = em.createQuery(
                    "SELECT b FROM Booking b " +
                            "WHERE b.room.id = :roomId " +
                            "AND b.checkOutDate > :checkIn " +
                            "AND b.checkInDate < :checkOut",
                    Booking.class
            );

            query.setParameter("roomId", roomId);
            query.setParameter("checkIn", checkIn);
            query.setParameter("checkOut", checkOut);

            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Booking> findActiveByRoomNumber(int roomNumber, LocalDate date) {
        return executeInsideTransaction(em ->
                em.createQuery("""
                                    SELECT b FROM Booking b
                                    JOIN b.room r
                                    WHERE r.number = :roomNumber
                                      AND b.checkInDate <= :date
                                      AND b.checkOutDate >= :date
                                """, Booking.class)
                        .setParameter("roomNumber", roomNumber)
                        .setParameter("date", date)
                        .getResultStream()
                        .findFirst()
        );
    }

    @Override
    public boolean existsFutureBookings(Long roomId, LocalDate fromDate) throws DAOException {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            Long count = em.createQuery("""
                            SELECT COUNT(b) FROM Booking b
                            WHERE b.room.id = :roomId
                              AND b.checkInDate >= :fromDate
                            """, Long.class)
                    .setParameter("roomId", roomId)
                    .setParameter("fromDate", fromDate)
                    .getSingleResult();

            return count > 0;
        } catch (Exception e) {
            log.error("Failed to check future bookings for room {}", roomId, e);
            throw new DAOException("Failed to check future bookings", e);
        } finally {
            em.close();
        }
    }
}
