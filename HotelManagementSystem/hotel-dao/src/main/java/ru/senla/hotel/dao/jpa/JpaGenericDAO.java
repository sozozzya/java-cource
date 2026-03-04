package ru.senla.hotel.dao.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.senla.hotel.dao.GenericDAO;
import ru.senla.hotel.dao.exception.DAOException;
import ru.senla.hotel.db.JpaUtil;

import java.util.List;

public abstract class JpaGenericDAO<T, ID> implements GenericDAO<T, ID> {

    private static final Logger log = LoggerFactory.getLogger(JpaGenericDAO.class);

    private final Class<T> entityClass;

    protected JpaGenericDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public T findById(ID id) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            log.debug("Finding {} by id={}", entityClass.getSimpleName(), id);
            return em.find(entityClass, id);
        } catch (Exception e) {
            log.error("findById failed for {} id={}", entityClass.getSimpleName(), id, e);
            throw new DAOException("Failed to find entity by id=" + id, e);
        } finally {
            em.close();
        }
    }

    @Override
    public List<T> findAll() {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            log.debug("Finding all {}", entityClass.getSimpleName());

            return em.createQuery(
                    "SELECT e FROM " + entityClass.getSimpleName() + " e",
                    entityClass
            ).getResultList();
        } catch (Exception e) {
            log.error("findAll failed for {}", entityClass.getSimpleName(), e);
            throw new DAOException("Failed to find all entities", e);
        } finally {
            em.close();
        }
    }

    @Override
    public T save(T entity) {
        return executeInsideTransaction(em -> {
            log.debug("Persisting {}", entityClass.getSimpleName());
            em.persist(entity);
            return entity;
        });
    }

    @Override
    public void update(T entity) {
        executeInsideTransaction(em -> {
            log.debug("Merging {}", entityClass.getSimpleName());
            em.merge(entity);
            return null;
        });
    }

    @Override
    public void deleteById(ID id) {
        executeInsideTransaction(em -> {
            log.debug("Deleting {} id={}", entityClass.getSimpleName(), id);
            T entity = em.find(entityClass, id);
            if (entity != null) {
                em.remove(entity);
            }
            return null;
        });
    }

    protected <R> R executeInsideTransaction(JpaCallback<R> callback) {
        EntityManager em = JpaUtil.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            R result = callback.execute(em);

            tx.commit();
            return result;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            log.error("Transaction failed for {}", entityClass.getSimpleName(), e);
            throw new DAOException("Transaction failed", e);
        } finally {
            em.close();
        }
    }

    @FunctionalInterface
    protected interface JpaCallback<R> {
        R execute(EntityManager em);
    }
}
