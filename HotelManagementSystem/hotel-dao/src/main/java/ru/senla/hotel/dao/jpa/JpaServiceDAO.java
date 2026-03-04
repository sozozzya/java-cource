package ru.senla.hotel.dao.jpa;

import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.senla.hotel.dao.ServiceDAO;
import ru.senla.hotel.db.JpaUtil;
import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.dao.exception.DAOException;
import ru.senla.hotel.model.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class JpaServiceDAO extends JpaGenericDAO<Service, Long> implements ServiceDAO {

    private static final Logger log = LoggerFactory.getLogger(JpaServiceDAO.class);

    public JpaServiceDAO() {
        super(Service.class);
    }

    @Override
    public Optional<Service> findByName(String name) throws DAOException {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT s FROM Service s WHERE LOWER(s.name) = LOWER(:name)",
                            Service.class
                    )
                    .setParameter("name", name)
                    .getResultStream()
                    .findFirst();
        } catch (Exception e) {
            log.error("Failed to find service by name: {}", name, e);
            throw new DAOException("Failed to find service by name=" + name, e);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Service> findByDate(LocalDate date) throws DAOException {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT s FROM Service s WHERE s.date = :date",
                            Service.class
                    )
                    .setParameter("date", date)
                    .getResultList();
        } catch (Exception e) {
            log.error("Failed to find services by date: {}", date, e);
            throw new DAOException("Failed to find services by date=" + date, e);
        } finally {
            em.close();
        }
    }
}
