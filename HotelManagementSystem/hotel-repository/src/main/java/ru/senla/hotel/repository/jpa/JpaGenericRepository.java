package ru.senla.hotel.repository.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import ru.senla.hotel.repository.GenericRepository;
import ru.senla.hotel.repository.exception.RepositoryException;

import java.util.List;


@Transactional
public abstract class JpaGenericRepository<T, ID> implements GenericRepository<T, ID> {

    private static final Logger log = LoggerFactory.getLogger(JpaGenericRepository.class);

    @PersistenceContext
    protected EntityManager em;

    private final Class<T> entityClass;

    protected JpaGenericRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public T findById(ID id) {
        try {
            log.debug("Finding {} by id={}", entityClass.getSimpleName(), id);
            return em.find(entityClass, id);
        } catch (Exception e) {
            log.error("findById failed", e);
            throw new RepositoryException("Failed to find entity", e);
        }
    }

    @Override
    public List<T> findAll() {
        try {
            return em.createQuery(
                    "SELECT e FROM " + entityClass.getSimpleName() + " e",
                    entityClass
            ).getResultList();
        } catch (Exception e) {
            log.error("findAll failed", e);
            throw new RepositoryException("Failed to find entities", e);
        }
    }

    @Override
    public T save(T entity) {
        try {
            em.persist(entity);
            return entity;
        } catch (Exception e) {
            log.error("save failed", e);
            throw new RepositoryException("Failed to save entity", e);
        }
    }

    @Override
    public void update(T entity) {
        try {
            em.merge(entity);
        } catch (Exception e) {
            log.error("update failed", e);
            throw new RepositoryException("Failed to update entity", e);
        }
    }
}
