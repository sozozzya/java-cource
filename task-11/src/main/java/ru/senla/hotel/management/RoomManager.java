package ru.senla.hotel.management;

import ru.senla.hotel.config.ApplicationConfig;
import ru.senla.hotel.dao.RoomDAO;
import ru.senla.hotel.db.ConnectionManager;
import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.exception.DAOException;
import ru.senla.hotel.exception.FeatureDisabledException;
import ru.senla.hotel.exception.room.*;
import ru.senla.hotel.model.Room;

import java.io.*;
import java.util.*;

@Component
public class RoomManager {

    @Inject
    private RoomDAO roomDAO;

    @Inject
    private ApplicationConfig config;

    @Inject
    private ConnectionManager connectionManager;

    public Optional<Room> findById(Long id) {
        return Optional.ofNullable(roomDAO.findById(id));
    }

    public Room getRoomByNumber(int number) {
        return roomDAO.findByNumber(number)
                .orElseThrow(() -> new RoomNotFoundException(number));
    }

    public List<Room> getAllRooms() {
        return roomDAO.findAll();
    }

    private void ensureRoomNotExists(Room room) {
        roomDAO.findByNumber(room.getNumber())
                .ifPresent(r -> {
                    throw new RoomAlreadyExistsException(room.getNumber());
                });
    }

    public Room addRoom(Room room) {
        try {
            ensureRoomNotExists(room);
            Room saved = roomDAO.save(room);

            connectionManager.commit();
            return saved;

        } catch (Exception e) {
            connectionManager.rollback();
            throw new RoomException("Failed to add room", e);
        }
    }

    public void addRoomsBatch(List<Room> rooms) {
        try {
            for (Room room : rooms) {
                ensureRoomNotExists(room);
                roomDAO.save(room);
            }

            connectionManager.commit();

        } catch (Exception e) {
            connectionManager.rollback();
            throw new RoomException("Failed to import rooms batch", e);
        }
    }

    public void changeRoomPrice(int roomNumber, double newPrice) {
        if (newPrice < 0) {
            throw new InvalidRoomPriceException(newPrice);
        }

        try {
            Room room = getRoomByNumber(roomNumber);
            room.setPricePerNight(newPrice);

            roomDAO.update(room);
            connectionManager.commit();

        } catch (Exception e) {
            connectionManager.rollback();
            throw new RoomException("Failed to change room price", e);
        }
    }

    public void setRoomMaintenance(int roomNumber, boolean status) {
        if (!config.isRoomStatusChangeEnabled()) {
            throw new FeatureDisabledException("Changing room maintenance status is disabled by configuration.");
        }

        try {
            Room room = getRoomByNumber(roomNumber);

            if (room.isUnderMaintenance() == status) {
                throw new RoomMaintenanceException(
                        "Room " + room.getNumber() +
                                (status
                                        ? " is already under maintenance."
                                        : " is not under maintenance.")
                );
            }

            room.setUnderMaintenance(status);
            roomDAO.update(room);

            connectionManager.commit();

        } catch (Exception e) {
            connectionManager.rollback();
            throw new RoomException("Failed to update room maintenance", e);
        }
    }

    public void exportRoomToCSV(String path) {
        if (path == null || path.isBlank()) {
            throw new RoomCsvException("CSV export path cannot be empty.");
        }

        File file = new File(path);

        if (file.isDirectory()) {
            file = new File(file, "rooms.csv");
        } else {
            if (!file.getName().toLowerCase().endsWith(".csv")) {
                throw new RoomCsvException(
                        "Invalid file format. CSV file expected: " + file.getName()
                );
            }
        }

        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println("id;number;capacity;stars;pricePerNight;isUnderMaintenance");
            roomDAO.findAll().forEach(g -> writer.println(g.toCsv()));
        } catch (IOException | DAOException e) {
            throw new RoomCsvException("Failed to export rooms: " + e.getMessage());
        }
    }

    public void importRoomFromCSV(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine();
            List<Room> rooms = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                rooms.add(Room.fromCsv(line));
            }
            addRoomsBatch(rooms);
        } catch (Exception e) {
            throw new RoomCsvException("Failed to import rooms: " + e.getMessage());
        }
    }
}
