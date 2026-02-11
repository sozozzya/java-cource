package ru.senla.hotel.dao.jdbc;

import ru.senla.hotel.dao.RoomDAO;
import ru.senla.hotel.db.ConnectionManager;
import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.dao.exception.DAOException;
import ru.senla.hotel.model.Room;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class JdbcRoomDAO implements RoomDAO {

    @Inject
    private ConnectionManager connectionManager;

    private static final String SQL_FIND_BY_ID =
            "SELECT * FROM rooms WHERE id = ?";

    private static final String SQL_FIND_BY_NUMBER =
            "SELECT * FROM rooms WHERE number = ?";

    private static final String SQL_FIND_ALL =
            "SELECT * FROM rooms";

    private static final String SQL_INSERT =
            "INSERT INTO rooms(number, capacity, stars, price_per_night, is_under_maintenance) " +
                    "VALUES (?, ?, ?, ?, ?)";

    private static final String SQL_UPDATE =
            "UPDATE rooms SET price_per_night = ?, is_under_maintenance = ? WHERE id = ?";

    private static final String SQL_DELETE =
            "DELETE FROM rooms WHERE id = ?";

    private Connection connection() {
        return connectionManager.getConnection();
    }

    @Override
    public Room findById(Long id) {
        try (PreparedStatement ps = connection()
                .prepareStatement(SQL_FIND_BY_ID)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRoom(rs) : null;
            }
        } catch (SQLException e) {
            throw new DAOException("Failed to find room by id=" + id, e);
        }
    }

    @Override
    public Optional<Room> findByNumber(int number) {
        try (PreparedStatement ps = connection()
                .prepareStatement(SQL_FIND_BY_NUMBER)) {
            ps.setInt(1, number);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next()
                        ? Optional.of(mapRoom(rs))
                        : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DAOException("Failed to find room by number=" + number, e);
        }
    }

    @Override
    public List<Room> findAll() {
        List<Room> rooms = new ArrayList<>();
        try (PreparedStatement ps = connection()
                .prepareStatement(SQL_FIND_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                rooms.add(mapRoom(rs));
            }
            return rooms;
        } catch (SQLException e) {
            throw new DAOException("Failed to find all rooms", e);
        }
    }

    @Override
    public Room save(Room room) {
        try (PreparedStatement ps = connection()
                .prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, room.getNumber());
            ps.setInt(2, room.getCapacity());
            ps.setInt(3, room.getStars());
            ps.setDouble(4, room.getPricePerNight());
            ps.setBoolean(5, room.isUnderMaintenance());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    room.setId(keys.getLong(1));
                }
            }
            return room;
        } catch (SQLException e) {
            throw new DAOException("Failed to save room", e);
        }
    }

    @Override
    public void update(Room room) {
        try (PreparedStatement ps = connection()
                .prepareStatement(SQL_UPDATE)) {
            ps.setDouble(1, room.getPricePerNight());
            ps.setBoolean(2, room.isUnderMaintenance());
            ps.setLong(3, room.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Failed to update room id=" + room.getId(), e);
        }
    }

    @Override
    public void deleteById(Long id) {
        try (PreparedStatement ps = connection()
                .prepareStatement(SQL_DELETE)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Failed to delete room id=" + id, e);
        }
    }

    private Room mapRoom(ResultSet rs) throws SQLException {
        Room room = new Room(
                rs.getLong("id"),
                rs.getInt("number"),
                rs.getInt("capacity"),
                rs.getInt("stars"),
                rs.getDouble("price_per_night")
        );
        room.setUnderMaintenance(rs.getBoolean("is_under_maintenance"));
        return room;
    }
}
