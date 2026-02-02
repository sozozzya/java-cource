package ru.senla.hotel.dao.jdbc;

import ru.senla.hotel.dao.GuestDAO;
import ru.senla.hotel.db.ConnectionManager;
import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.exception.DAOException;
import ru.senla.hotel.model.Guest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class JdbcGuestDAO implements GuestDAO {

    @Inject
    private ConnectionManager connectionManager;

    private static final String SQL_FIND_BY_ID =
            "SELECT * FROM guests WHERE id = ?";

    private static final String SQL_FIND_BY_NAME =
            "SELECT * FROM guests WHERE LOWER(name) = LOWER(?)";

    private static final String SQL_FIND_ALL =
            "SELECT * FROM guests";

    private static final String SQL_INSERT =
            "INSERT INTO guests (name) VALUES (?)";

    private static final String SQL_UPDATE =
            "UPDATE guests SET name = ? WHERE id = ?";

    private static final String SQL_DELETE =
            "DELETE FROM guests WHERE id = ?";

    private Connection connection() {
        return connectionManager.getConnection();
    }

    @Override
    public Guest findById(Long id) {
        try (PreparedStatement ps = connection().prepareStatement(SQL_FIND_BY_ID)) {
            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }

        } catch (SQLException e) {
            throw new DAOException("Failed to find guest by id=" + id, e);
        }
    }

    @Override
    public List<Guest> findAll() {
        List<Guest> result = new ArrayList<>();

        try (PreparedStatement ps = connection().prepareStatement(SQL_FIND_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(mapRow(rs));
            }
            return result;

        } catch (SQLException e) {
            throw new DAOException("Failed to find all guests", e);
        }
    }

    @Override
    public Optional<Guest> findByName(String name) {
        try (PreparedStatement ps = connection().prepareStatement(SQL_FIND_BY_NAME)) {
            ps.setString(1, name);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next()
                        ? Optional.of(mapRow(rs))
                        : Optional.empty();
            }

        } catch (SQLException e) {
            throw new DAOException("Failed to find guest by name=" + name, e);
        }
    }

    @Override
    public Guest save(Guest guest) {
        try (PreparedStatement ps = connection().prepareStatement(
                SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, guest.getName());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    guest.setId(keys.getLong(1));
                }
            }
            return guest;

        } catch (SQLException e) {
            throw new DAOException("Failed to save guest", e);
        }
    }

    @Override
    public void update(Guest guest) {
        try (PreparedStatement ps = connection().prepareStatement(SQL_UPDATE)) {
            ps.setString(1, guest.getName());
            ps.setLong(2, guest.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Failed to update guest id=" + guest.getId(), e);
        }
    }

    @Override
    public void deleteById(Long id) {
        try (PreparedStatement ps = connection().prepareStatement(SQL_DELETE)) {
            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Failed to delete guest id=" + id, e);
        }
    }

    private Guest mapRow(ResultSet rs) throws SQLException {
        return new Guest(
                rs.getLong("id"),
                rs.getString("name")
        );
    }
}
