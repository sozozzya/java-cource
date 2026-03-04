package ru.senla.hotel.dao.jdbc;

import ru.senla.hotel.dao.BookingDAO;
import ru.senla.hotel.db.ConnectionManager;
import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.exception.DAOException;
import ru.senla.hotel.model.Booking;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class JdbcBookingDAO implements BookingDAO {

    @Inject
    private ConnectionManager connectionManager;

    private static final String SQL_FIND_BY_ID =
            "SELECT * FROM bookings WHERE id=?";

    private static final String SQL_FIND_ALL =
            "SELECT * FROM bookings";

    private static final String SQL_FIND_ACTIVE_BY_DATE =
            "SELECT * FROM bookings WHERE check_in_date <= ? AND check_out_date >= ?";

    private static final String SQL_FIND_ACTIVE_BY_ROOM =
            "SELECT * FROM bookings WHERE room_id = ? AND check_in_date <= ? AND check_out_date >= ?";

    private static final String SQL_FIND_BY_GUEST =
            "SELECT * FROM bookings WHERE guest_id = ?";

    private static final String SQL_INSERT =
            "INSERT INTO bookings (guest_id, room_id, check_in_date, check_out_date) VALUES (?, ?, ?, ?)";

    private static final String SQL_UPDATE_CHECKOUT =
            "UPDATE bookings SET check_out_date=? WHERE id=?";

    private static final String SQL_INSERT_SERVICE =
            "INSERT INTO booking_services (booking_id, service_id) VALUES (?, ?)";

    private static final String SQL_FIND_SERVICE_IDS =
            "SELECT service_id FROM booking_services WHERE booking_id=?";

    private static final String SQL_DELETE =
            "DELETE FROM bookings WHERE id=?";

    private Connection connection() {
        return connectionManager.getConnection();
    }

    @Override
    public Booking findById(Long id) {
        try (PreparedStatement ps = connection().prepareStatement(SQL_FIND_BY_ID)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException e) {
            throw new DAOException("Find booking by id failed", e);
        }
    }

    @Override
    public List<Booking> findAll() {
        List<Booking> list = new ArrayList<>();
        try (PreparedStatement ps = connection().prepareStatement(SQL_FIND_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
            return list;
        } catch (SQLException e) {
            throw new DAOException("Find all bookings failed", e);
        }
    }

    @Override
    public List<Booking> findActiveByDate(LocalDate date) {
        List<Booking> list = new ArrayList<>();
        try (PreparedStatement ps = connection().prepareStatement(SQL_FIND_ACTIVE_BY_DATE)) {
            ps.setDate(1, Date.valueOf(date));
            ps.setDate(2, Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new DAOException("Find active bookings failed", e);
        }
    }

    @Override
    public Optional<Booking> findActiveByRoomId(Long roomId, LocalDate date) {
        try (PreparedStatement ps = connection().prepareStatement(SQL_FIND_ACTIVE_BY_ROOM)) {
            ps.setLong(1, roomId);
            ps.setDate(2, Date.valueOf(date));
            ps.setDate(3, Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(map(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DAOException("Find active booking by room failed", e);
        }
    }

    @Override
    public List<Booking> findByGuestId(Long guestId) {
        List<Booking> list = new ArrayList<>();
        try (PreparedStatement ps = connection().prepareStatement(SQL_FIND_BY_GUEST)) {
            ps.setLong(1, guestId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new DAOException("Find bookings by guest failed", e);
        }
    }

    @Override
    public Booking save(Booking booking) {
        try (PreparedStatement ps = connection().prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, booking.getGuestId());
            ps.setLong(2, booking.getRoomId());
            ps.setDate(3, Date.valueOf(booking.getCheckInDate()));
            ps.setDate(4, Date.valueOf(booking.getCheckOutDate()));
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) booking.setId(keys.getLong(1));
            }

            return booking;
        } catch (SQLException e) {
            throw new DAOException("Save booking failed", e);
        }
    }

    @Override
    public void update(Booking booking) {
        try (PreparedStatement ps = connection().prepareStatement(SQL_UPDATE_CHECKOUT)) {
            ps.setDate(1, Date.valueOf(booking.getCheckOutDate()));
            ps.setLong(2, booking.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Update booking failed", e);
        }
    }

    @Override
    public void addServiceToBooking(Long bookingId, Long serviceId) {
        try (PreparedStatement ps = connection().prepareStatement(SQL_INSERT_SERVICE)) {
            ps.setLong(1, bookingId);
            ps.setLong(2, serviceId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Add service to booking failed", e);
        }
    }

    @Override
    public List<Long> findServiceIdsByBooking(Long bookingId) {
        List<Long> ids = new ArrayList<>();
        try (PreparedStatement ps = connection().prepareStatement(SQL_FIND_SERVICE_IDS)) {
            ps.setLong(1, bookingId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) ids.add(rs.getLong(1));
            }
            return ids;
        } catch (SQLException e) {
            throw new DAOException("Find booking services failed", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        try (PreparedStatement ps = connection().prepareStatement(SQL_DELETE)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Delete booking failed", e);
        }
    }

    private Booking map(ResultSet rs) throws SQLException {
        Booking b = new Booking(
                rs.getLong("id"),
                null,
                null,
                rs.getDate("check_in_date").toLocalDate(),
                rs.getDate("check_out_date").toLocalDate()
        );
        b.setGuestId(rs.getLong("guest_id"));
        b.setRoomId(rs.getLong("room_id"));
        return b;
    }
}
