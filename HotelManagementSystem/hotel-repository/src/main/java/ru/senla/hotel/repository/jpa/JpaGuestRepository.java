package ru.senla.hotel.repository.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import ru.senla.hotel.repository.GuestRepository;
import ru.senla.hotel.repository.exception.RepositoryException;
import ru.senla.hotel.domain.model.Guest;

import java.util.Optional;

@Repository
public class JpaGuestRepository extends JpaGenericRepository<Guest, Long> implements GuestRepository {

    private static final Logger log = LoggerFactory.getLogger(JpaGuestRepository.class);

    public JpaGuestRepository() {
        super(Guest.class);
    }

    @Override
    public Optional<Guest> findByName(String name) {
        log.debug("Attempting to find guest by name: {}", name);
        try {
            Optional<Guest> result = em.createQuery(
                            "SELECT g FROM Guest g WHERE LOWER(g.name) = LOWER(:name)",
                            Guest.class)
                    .setParameter("name", name)
                    .getResultStream().
                    findFirst();

            log.debug("Guest found: {}", result.isPresent());
            return result;
        } catch (DataAccessException e) {
            log.error("Database error while finding guest by name: {}", name, e);
            throw new RepositoryException("Failed to find guest by name=" + name, e);
        } catch (Exception e) {
            log.error("Unexpected error while finding guest by name: {}", name, e);
            throw new RepositoryException("Unexpected error during guest search", e);
        }
    }
}