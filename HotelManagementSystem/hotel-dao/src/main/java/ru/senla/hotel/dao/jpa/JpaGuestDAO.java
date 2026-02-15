package ru.senla.hotel.dao.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.senla.hotel.dao.GuestDAO;
import ru.senla.hotel.db.JpaUtil;
import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.dao.exception.DAOException;
import ru.senla.hotel.model.Guest;

import java.util.Optional;

@Component
public class JpaGuestDAO extends JpaGenericDAO<Guest, Long> implements GuestDAO {

    private static final Logger log = LoggerFactory.getLogger(JpaGuestDAO.class);

    public JpaGuestDAO() {
        super(Guest.class);
    }

    @Override
    public Optional<Guest> findByName(String name) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            TypedQuery<Guest> query = em.createQuery(
                    "SELECT g FROM Guest g WHERE LOWER(g.name) = LOWER(:name)",
                    Guest.class
            );

            query.setParameter("name", name);

            return query.getResultStream().findFirst();
        } catch (Exception e) {
            log.error("Failed to find guest by name: {}", name, e);
            throw new DAOException("Failed to find guest by name=" + name, e);
        } finally {
            em.close();
        }
    }
}
