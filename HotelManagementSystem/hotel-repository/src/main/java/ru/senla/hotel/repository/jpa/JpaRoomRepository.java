package ru.senla.hotel.repository.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import ru.senla.hotel.repository.RoomRepository;
import ru.senla.hotel.domain.model.Room;

import java.util.Optional;

import org.springframework.stereotype.Repository;
import ru.senla.hotel.repository.exception.RepositoryException;

@Repository
public class JpaRoomRepository extends JpaGenericRepository<Room, Long> implements RoomRepository {

    private static final Logger log = LoggerFactory.getLogger(JpaRoomRepository.class);

    public JpaRoomRepository() {
        super(Room.class);
    }

    @Override
    public Optional<Room> findByNumber(int number) {
        log.debug("Attempting to find room by number: {}", number);
        try {
            Optional<Room> result = em.createQuery(
                            "SELECT r FROM Room r WHERE r.number = :number",
                            Room.class
                    )
                    .setParameter("number", number)
                    .getResultStream()
                    .findFirst();

            log.debug("Room found: {}", result.isPresent());
            return result;
        } catch (DataAccessException e) {
            log.error("Database error while finding room by number: {}", number, e);
            throw new RepositoryException("Failed to find room by number=" + number, e);
        } catch (Exception e) {
            log.error("Unexpected error while finding room by number: {}", number, e);
            throw new RepositoryException("Unexpected error during room search", e);
        }
    }
}
