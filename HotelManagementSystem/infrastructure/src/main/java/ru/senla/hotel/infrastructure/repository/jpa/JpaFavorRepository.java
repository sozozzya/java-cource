package ru.senla.hotel.infrastructure.repository.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import ru.senla.hotel.domain.entity.Favor;
import ru.senla.hotel.infrastructure.repository.FavorRepository;
import ru.senla.hotel.infrastructure.exception.RepositoryException;

import java.util.Optional;

@Repository
public class JpaFavorRepository extends JpaGenericRepository<Favor, Long> implements FavorRepository {

    private static final Logger log = LoggerFactory.getLogger(JpaFavorRepository.class);

    public JpaFavorRepository() {
        super(Favor.class);
    }

    @Override
    public Optional<Favor> findByName(String name) {
        log.debug("Attempting to find favor by name: {}", name);
        try {
            Optional<Favor> result = em.createQuery("""
                            SELECT f
                            FROM Favor f
                            WHERE LOWER(f.name) = LOWER(:name)
                            """, Favor.class)
                    .setParameter("name", name)
                    .getResultStream()
                    .findFirst();

            log.debug("Favor found: {}", result.isPresent());
            return result;
        } catch (DataAccessException e) {
            log.error("Database error while finding favor by name: {}", name, e);
            throw new RepositoryException("Failed to find favor by name=" + name, e);
        } catch (Exception e) {
            log.error("Unexpected error while finding favor by name: {}", name, e);
            throw new RepositoryException("Unexpected error during favor search", e);
        }
    }
}
