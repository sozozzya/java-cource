package ru.senla.hotel.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.senla.hotel.ApplicationConfig;
import ru.senla.hotel.dao.RoomDAO;
import ru.senla.hotel.db.ConnectionManager;
import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.dao.exception.DAOException;
import ru.senla.hotel.service.exception.FeatureDisabledException;
import ru.senla.hotel.model.Room;
import ru.senla.hotel.service.exception.room.RoomException;
import ru.senla.hotel.service.exception.room.InvalidRoomPriceException;
import ru.senla.hotel.service.exception.room.RoomCsvException;
import ru.senla.hotel.service.exception.room.RoomMaintenanceException;
import ru.senla.hotel.service.exception.room.RoomAlreadyExistsException;
import ru.senla.hotel.service.exception.room.RoomNotFoundException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class RoomManager {

    @Inject
    private RoomDAO roomDAO;

    @Inject
    private ApplicationConfig config;

    @Inject
    private ConnectionManager connectionManager;

    private static final Logger log = LoggerFactory.getLogger(RoomManager.class);

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
        log.info("Start adding room: number={}, capacity={}, stars={}",
                room.getNumber(), room.getCapacity(), room.getStars());

        try {
            ensureRoomNotExists(room);
            Room saved = roomDAO.save(room);

            connectionManager.commit();

            log.info("Room successfully added: id={}, number={}",
                    saved.getId(), saved.getNumber());

            return saved;
        } catch (Exception e) {
            connectionManager.rollback();
            log.error("Failed to add room: number={}", room.getNumber(), e);
            throw new RoomException("Failed to add room", e);
        }
    }

    public void addRoomsBatch(List<Room> rooms) {
        log.info("Start importing rooms batch: size={}", rooms.size());

        try {
            for (Room room : rooms) {
                ensureRoomNotExists(room);
                roomDAO.save(room);
            }

            connectionManager.commit();

            log.info("Rooms batch successfully imported: size={}", rooms.size());
        } catch (Exception e) {
            connectionManager.rollback();
            log.error("Failed to import rooms batch: size={}", rooms.size(), e);
            throw new RoomException("Failed to import rooms batch", e);
        }
    }

    public void changeRoomPrice(int roomNumber, double newPrice) {
        log.info("Start changing room price: roomNumber={}, newPrice={}",
                roomNumber, newPrice);

        if (newPrice < 0) {
            log.error("Invalid room price: {}", newPrice);
            throw new InvalidRoomPriceException(newPrice);
        }

        try {
            Room room = getRoomByNumber(roomNumber);
            room.setPricePerNight(newPrice);

            roomDAO.update(room);
            connectionManager.commit();

            log.info("Room price successfully changed: roomNumber={}, newPrice={}",
                    roomNumber, newPrice);
        } catch (Exception e) {
            connectionManager.rollback();
            log.error("Failed to change room price: roomNumber={}", roomNumber, e);
            throw new RoomException("Failed to change room price", e);
        }
    }

    public void setRoomMaintenance(int roomNumber, boolean status) {
        log.info("Start changing room maintenance status: roomNumber={}, status={}",
                roomNumber, status);

        if (!config.isRoomStatusChangeEnabled()) {
            log.error("Room maintenance change disabled by configuration");
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

            log.info("Room maintenance status successfully changed: roomNumber={}, status={}",
                    roomNumber, status);
        } catch (Exception e) {
            connectionManager.rollback();
            log.error("Failed to change room maintenance status: roomNumber={}", roomNumber, e);
            throw new RoomException("Failed to update room maintenance", e);
        }
    }

    public void exportRoomToCSV(String path) {
        log.info("Start exporting rooms to CSV: path={}", path);

        if (path == null || path.isBlank()) {
            log.error("CSV export path is empty");
            throw new RoomCsvException("CSV export path cannot be empty.");
        }

        File file = new File(path);

        if (file.isDirectory()) {
            file = new File(file, "rooms.csv");
        } else {
            if (!file.getName().toLowerCase().endsWith(".csv")) {
                log.error("Invalid CSV file name: {}", file.getName());
                throw new RoomCsvException("Invalid file format. CSV file expected: " + file.getName());
            }
        }

        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println("id;number;capacity;stars;pricePerNight;isUnderMaintenance");
            roomDAO.findAll().forEach(g -> writer.println(g.toCsv()));

            log.info("Rooms successfully exported to CSV: file={}", file.getAbsolutePath());
        } catch (IOException | DAOException e) {
            log.error("Failed to export rooms to CSV: path={}", path, e);
            throw new RoomCsvException("Failed to export rooms: " + e.getMessage());
        }
    }

    public void importRoomFromCSV(String path) {
        log.info("Start importing rooms from CSV: path={}", path);

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine();
            List<Room> rooms = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                rooms.add(Room.fromCsv(line));
            }
            addRoomsBatch(rooms);

            log.info("Rooms successfully imported from CSV: count={}", rooms.size());
        } catch (Exception e) {
            log.error("Failed to import rooms from CSV: path={}", path, e);
            throw new RoomCsvException("Failed to import rooms: " + e.getMessage());
        }
    }
}
