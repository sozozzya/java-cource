package ru.senla.hotel.dao.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.senla.hotel.dao.BookingServiceDAO;
import ru.senla.hotel.dao.exception.DAOException;
import ru.senla.hotel.db.JpaUtil;
import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.model.BookingService;

import java.util.List;

@Component
public class JpaBookingServiceDAO extends JpaGenericDAO<BookingService, Long> implements BookingServiceDAO {

    private static final Logger log = LoggerFactory.getLogger(JpaBookingServiceDAO.class);

    public JpaBookingServiceDAO() {
        super(BookingService.class);
    }

    @Override
    public void removeServiceFromBooking(Long bookingId, Long serviceId) throws DAOException {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createQuery("""
                            DELETE FROM BookingService bs
                            WHERE bs.booking.id = :bookingId
                            AND bs.service.id = :serviceId
                            """)
                    .setParameter("bookingId", bookingId)
                    .setParameter("serviceId", serviceId)
                    .executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new DAOException("Remove service failed", e);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Long> findServiceIdsByBooking(Long bookingId) throws DAOException {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            return em.createQuery("""
                            SELECT bs.service.id
                            FROM BookingService bs
                            WHERE bs.booking.id = :bookingId
                            """, Long.class)
                    .setParameter("bookingId", bookingId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<BookingService> findServicesByGuestName(String guestName) throws DAOException {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            TypedQuery<BookingService> query = em.createQuery("""
                    SELECT bs
                    FROM BookingService bs
                    JOIN FETCH bs.service s
                    JOIN bs.booking b
                    JOIN b.guest g
                    WHERE lower(g.name) = lower(:guestName)
                    """, BookingService.class);
            query.setParameter("guestName", guestName);
            return query.getResultList();
        } catch (Exception e) {
            throw new DAOException("Failed to fetch guest services", e);
        } finally {
            em.close();
        }
    }
}

