package ru.senla.hotel.infrastructure.repository.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import ru.senla.hotel.domain.entity.User;
import ru.senla.hotel.infrastructure.exception.RepositoryException;
import ru.senla.hotel.infrastructure.repository.UserRepository;

import java.util.Optional;

@Repository
public class JpaUserRepository implements UserRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<User> findByUsername(String username) {
        try {
            User user = em.createQuery(
                            "SELECT u FROM User u WHERE u.username = :username",
                            User.class
                    )
                    .setParameter("username", username)
                    .getSingleResult();

            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (Exception e) {
            throw new RepositoryException(
                    "Failed to find user by username=" + username, e
            );
        }
    }

    @Override
    public User save(User user) {
        try {
            if (user.getId() == null) {
                em.persist(user);
                return user;
            } else {
                return em.merge(user);
            }
        } catch (Exception e) {
            throw new RepositoryException("Failed to save user", e);
        }
    }
}
