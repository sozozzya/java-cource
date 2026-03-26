package ru.senla.hotel.infrastructure.repository.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import ru.senla.hotel.domain.entity.BookingFavor;
import ru.senla.hotel.infrastructure.repository.BookingFavorRepository;
import ru.senla.hotel.infrastructure.exception.RepositoryException;

import java.util.List;

@Repository
public class JpaBookingFavorRepository extends JpaGenericRepository<BookingFavor, Long> implements BookingFavorRepository {

    private static final Logger log = LoggerFactory.getLogger(JpaBookingFavorRepository.class);

    public JpaBookingFavorRepository() {
        super(BookingFavor.class);
    }

    @Override
    public List<BookingFavor> findFavorsByGuestName(String guestName) {
        log.debug("Attempting to find favors by guest name: {}", guestName);
        try {
            List<BookingFavor> result = em.createQuery("""
                            SELECT bf
                            FROM BookingFavor bf
                            JOIN FETCH bf.favor f
                            JOIN FETCH bf.booking b
                            JOIN b.guest g
                            WHERE LOWER(g.name) = LOWER(:guestName)
                            """, BookingFavor.class)
                    .setParameter("guestName", guestName)
                    .getResultList();

            log.debug("Favors found: {}", result.size());
            return result;
        } catch (DataAccessException e) {
            log.error("Database error while finding favors by guest name: {}", guestName, e);
            throw new RepositoryException("Failed to find favors by guest name: {}" + guestName, e);
        } catch (Exception e) {
            log.error("Unexpected error while finding favors by guest name: {}", guestName, e);
            throw new RepositoryException("Unexpected error during favors search", e);
        }
    }
}
