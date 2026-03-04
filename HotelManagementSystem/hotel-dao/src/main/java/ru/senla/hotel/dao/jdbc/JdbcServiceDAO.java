package ru.senla.hotel.dao.jdbc;

import ru.senla.hotel.dao.ServiceDAO;
import ru.senla.hotel.db.ConnectionManager;
import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.dao.exception.DAOException;
import ru.senla.hotel.model.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class JdbcServiceDAO implements ServiceDAO {

    @Inject
    private ConnectionManager connectionManager;

    private static final String SQL_FIND_BY_ID =
            "SELECT * FROM services WHERE id = ?";

    private static final String SQL_FIND_BY_NAME =
            "SELECT * FROM services WHERE LOWER(name) = LOWER(?)";

    private static final String SQL_FIND_BY_DATE =
            "SELECT * FROM services WHERE date = ?";

    private static final String SQL_FIND_ALL =
            "SELECT * FROM services";

    private static final String SQL_INSERT =
            "INSERT INTO services (name, price, date) VALUES (?, ?, ?)";

    private static final String SQL_UPDATE =
            "UPDATE services SET price = ? WHERE id = ?";

    private static final String SQL_DELETE =
            "DELETE FROM services WHERE id = ?";

    private Connection connection() {
        return connectionManager.getConnection();
    }

    @Override
    public Service findById(Long id) {
        try (PreparedStatement ps = connection().prepareStatement(SQL_FIND_BY_ID)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? mapRow(rs) : null;
        } catch (SQLException e) {
            throw new DAOException("Failed to find service by id=" + id, e);
        }
    }

    @Override
    public List<Service> findAll() {
        List<Service> result = new ArrayList<>();
        try (PreparedStatement ps = connection().prepareStatement(SQL_FIND_ALL)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(mapRow(rs));
            }
            return result;
        } catch (SQLException e) {
            throw new DAOException("Failed to find all services", e);
        }
    }

    @Override
    public Optional<Service> findByName(String name) {
        try (PreparedStatement ps = connection().prepareStatement(SQL_FIND_BY_NAME)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            return rs.next()
                    ? Optional.of(mapRow(rs))
                    : Optional.empty();
        } catch (SQLException e) {
            throw new DAOException("Failed to find service by name=" + name, e);
        }
    }

    @Override
    public List<Service> findByDate(LocalDate date) {
        List<Service> result = new ArrayList<>();
        try (PreparedStatement ps = connection().prepareStatement(SQL_FIND_BY_DATE)) {
            ps.setDate(1, Date.valueOf(date));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(mapRow(rs));
            }
            return result;
        } catch (SQLException e) {
            throw new DAOException("Failed to find services by date=" + date, e);
        }
    }

    @Override
    public Service save(Service service) {
        try (PreparedStatement ps = connection().prepareStatement(
                SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, service.getName());
            ps.setDouble(2, service.getPrice());
            ps.setDate(3, Date.valueOf(service.getDate()));
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                service.setId(keys.getLong(1));
            }
            return service;
        } catch (SQLException e) {
            throw new DAOException("Failed to save service", e);
        }
    }

    @Override
    public void update(Service service) {
        try (PreparedStatement ps = connection().prepareStatement(SQL_UPDATE)) {
            ps.setDouble(1, service.getPrice());
            ps.setLong(2, service.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Failed to update service id=" + service.getId(), e);
        }
    }

    @Override
    public void deleteById(Long id) {
        try (PreparedStatement ps = connection().prepareStatement(SQL_DELETE)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Failed to delete service id=" + id, e);
        }
    }

    private Service mapRow(ResultSet rs) throws SQLException {
        return new Service(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getDouble("price"),
                rs.getDate("date").toLocalDate()
        );
    }
}
