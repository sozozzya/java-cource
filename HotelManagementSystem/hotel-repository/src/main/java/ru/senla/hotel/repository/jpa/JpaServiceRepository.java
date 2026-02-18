package ru.senla.hotel.repository.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import ru.senla.hotel.repository.ServiceRepository;
import ru.senla.hotel.repository.exception.RepositoryException;
import ru.senla.hotel.domain.model.HotelService;

import java.util.Optional;

@Repository
public class JpaServiceRepository extends JpaGenericRepository<HotelService, Long> implements ServiceRepository {

    private static final Logger log = LoggerFactory.getLogger(JpaServiceRepository.class);

    public JpaServiceRepository() {
        super(HotelService.class);
    }

    @Override
    public Optional<HotelService> findByName(String name) {
        log.debug("Attempting to find service by name: {}", name);
        try {
            Optional<HotelService> result = em.createQuery(
                            "SELECT s FROM service s WHERE LOWER(s.name) = LOWER(:name)",
                            HotelService.class)
                    .setParameter("name", name)
                    .getResultStream()
                    .findFirst();

            log.debug("Service found: {}", result.isPresent());
            return result;
        } catch (DataAccessException e) {
            log.error("Database error while finding service by name: {}", name, e);
            throw new RepositoryException("Failed to find service by name=" + name, e);
        } catch (Exception e) {
            log.error("Unexpected error while finding service by name: {}", name, e);
            throw new RepositoryException("Unexpected error during service search", e);
        }
    }
}
