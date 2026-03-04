package ru.senla.hotel.dao.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.senla.hotel.dao.RoomDAO;
import ru.senla.hotel.db.JpaUtil;
import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.dao.exception.DAOException;
import ru.senla.hotel.model.Room;

import java.util.Optional;

@Component
public class JpaRoomDAO extends JpaGenericDAO<Room, Long> implements RoomDAO {

    private static final Logger log = LoggerFactory.getLogger(JpaRoomDAO.class);

    public JpaRoomDAO() {
        super(Room.class);
    }

    @Override
    public Optional<Room> findByNumber(int number) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            TypedQuery<Room> query = em.createQuery(
                    "SELECT r FROM Room r WHERE r.number = :number",
                    Room.class
            );
            query.setParameter("number", number);
            return query.getResultStream().findFirst();
        } catch (Exception e) {
            log.error("findByNumber failed", e);
            throw new DAOException("Failed to find room by number", e);
        } finally {
            em.close();
        }
    }
}
