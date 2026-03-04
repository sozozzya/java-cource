package ru.senla.hotel.repository.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import ru.senla.hotel.repository.BookingServiceRepository;
import ru.senla.hotel.repository.exception.RepositoryException;
import ru.senla.hotel.domain.model.BookingService;

import java.util.List;

@Repository
public class JpaBookingServiceRepository extends JpaGenericRepository<BookingService, Long> implements BookingServiceRepository {

    private static final Logger log = LoggerFactory.getLogger(JpaBookingServiceRepository.class);

    public JpaBookingServiceRepository() {
        super(BookingService.class);
    }

    @Override
    public List<BookingService> findServicesByGuestName(String guestName) {
        log.debug("Attempting to find services by guest name: {}", guestName);
        try {
            List<BookingService> result = em.createQuery("""
                            SELECT bs
                            FROM BookingService bs
                            JOIN FETCH bs.service s
                            JOIN bs.booking b
                            JOIN b.guest g
                            WHERE LOWER(g.name) = LOWER(:guestName)
                            """, BookingService.class)
                    .setParameter("guestName", guestName)
                    .getResultList();

            log.debug("Services found: {}", result.size());
            return result;
        } catch (DataAccessException e) {
            log.error("Database error while finding services by guest name: {}", guestName, e);
            throw new RepositoryException("Failed to find services by guest name: {}" + guestName, e);
        } catch (Exception e) {
            log.error("Unexpected error while finding services by guest name: {}", guestName, e);
            throw new RepositoryException("Unexpected error during services search", e);
        }
    }
}
