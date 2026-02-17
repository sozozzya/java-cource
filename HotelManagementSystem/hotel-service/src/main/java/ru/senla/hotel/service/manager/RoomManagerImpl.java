package ru.senla.hotel.service.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.senla.hotel.config.ApplicationConfig;
import ru.senla.hotel.repository.BookingRepository;
import ru.senla.hotel.repository.RoomRepository;
import ru.senla.hotel.repository.exception.RepositoryException;
import ru.senla.hotel.service.RoomManager;
import ru.senla.hotel.service.exception.FeatureDisabledException;
import ru.senla.hotel.domain.model.Room;
import ru.senla.hotel.service.exception.room.RoomNotFoundException;
import ru.senla.hotel.service.exception.room.RoomException;
import ru.senla.hotel.service.exception.room.RoomOccupiedException;
import ru.senla.hotel.service.exception.room.RoomCsvException;
import ru.senla.hotel.service.exception.room.RoomMaintenanceException;
import ru.senla.hotel.service.exception.room.RoomAlreadyExistsException;
import ru.senla.hotel.service.exception.room.InvalidRoomPriceException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RoomManagerImpl implements RoomManager {

    private static final Logger log = LoggerFactory.getLogger(RoomManagerImpl.class);

    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    private final ApplicationConfig config;

    public RoomManagerImpl(RoomRepository roomRepository,
                           BookingRepository bookingRepository,
                           ApplicationConfig config) {
        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
        this.config = config;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Room> findById(Long id) {
        log.debug("Finding room by id: {}", id);
        try {
            return Optional.ofNullable(roomRepository.findById(id));
        } catch (RepositoryException e) {
            log.error("Failed to find room by id: {}", id, e);
            throw new RoomException("Failed to find room", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Room getRoomByNumber(int number) {
        log.debug("Finding room by number: {}", number);

        return roomRepository.findByNumber(number)
                .orElseThrow(() -> {
                    log.warn("Room not found: {}", number);
                    return new RoomNotFoundException(number);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public List<Room> getAllRooms() {
        log.debug("Fetching all rooms");
        return roomRepository.findAll();
    }

    @Override
    public Room addRoom(Room room) {
        log.info("Start adding room: number={}, capacity={}, stars={}",
                room.getNumber(), room.getCapacity(), room.getStars());

        try {
            ensureRoomNotExists(room);
            Room saved = roomRepository.save(room);

            log.info("Room successfully added: id={}, number={}", saved.getId(), saved.getNumber());
            return saved;
        } catch (Exception e) {
            log.error("Failed to add room: number={}", room.getNumber(), e);
            throw new RoomException("Failed to add room", e);
        }
    }

    @Override
    public void addRoomsBatch(List<Room> rooms) {
        log.info("Start importing rooms batch: size={}", rooms.size());

        try {
            for (Room room : rooms) {
                ensureRoomNotExists(room);
                roomRepository.save(room);
            }

            log.info("Rooms batch successfully imported: size={}", rooms.size());
        } catch (Exception e) {
            log.error("Failed to import rooms batch: size={}", rooms.size(), e);
            throw new RoomException("Failed to import rooms batch", e);
        }
    }

    @Override
    public void changeRoomPrice(int roomNumber, BigDecimal newPrice) {
        log.info("Start changing room price: roomNumber={}, newPrice={}",
                roomNumber, newPrice);

        if (newPrice.compareTo(BigDecimal.ZERO) < 0) {
            log.warn("Invalid room price: {}", newPrice);
            throw new InvalidRoomPriceException(newPrice);
        }

        try {
            Room room = getRoomByNumber(roomNumber);
            room.setPricePerNight(newPrice);

            roomRepository.update(room);
            log.info("Room price successfully changed: roomNumber={}, newPrice={}", roomNumber, newPrice);
        } catch (Exception e) {
            log.error("Failed to change room price: roomNumber={}", roomNumber, e);
            throw new RoomException("Failed to change room price", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isRoomStatusChangeEnabled() {
        return config.isRoomStatusChangeEnabled();
    }

    @Override
    public void changeMaintenanceStatus(int roomNumber, boolean status) {
        log.info("Start changing room maintenance status: roomNumber={}, status={}", roomNumber, status);

        if (!isRoomStatusChangeEnabled()) {
            log.warn("Maintenance status change disabled by config");
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

            if (status && bookingRepository.existsFutureBookings(room.getId(), LocalDate.now())) {
                throw new RoomOccupiedException(roomNumber);
            }

            room.setUnderMaintenance(status);
            roomRepository.update(room);

            log.info("Room maintenance status successfully changed: roomNumber={}, status={}", roomNumber, status);
        } catch (Exception e) {
            log.error("Failed to change room maintenance status: roomNumber={}", roomNumber, e);
            throw new RoomException("Failed to update room maintenance", e);
        }
    }

    @Override
    public void exportRoomToCSV(String path) {
        log.info("Start exporting rooms to CSV: path={}", path);

        if (path == null || path.isBlank()) {
            log.warn("CSV export path is empty");
            throw new RoomCsvException("CSV export path cannot be empty.");
        }

        File file = new File(path);

        if (file.isDirectory()) {
            file = new File(file, "rooms.csv");
        } else {
            if (!file.getName().toLowerCase().endsWith(".csv")) {
                log.warn("Invalid CSV file name: {}", file.getName());
                throw new RoomCsvException("Invalid file format. CSV file expected: " + file.getName());
            }
        }

        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println("id;number;capacity;stars;pricePerNight;isUnderMaintenance");
            roomRepository.findAll().forEach(g -> writer.println(g.toCsv()));

            log.info("Rooms successfully exported to CSV: file={}", file.getAbsolutePath());
        } catch (IOException | RepositoryException e) {
            log.error("Failed to export rooms to CSV: path={}", path, e);
            throw new RoomCsvException("Failed to export rooms: " + e.getMessage());
        }
    }

    @Override
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

    private void ensureRoomNotExists(Room room) {
        roomRepository.findByNumber(room.getNumber())
                .ifPresent(r -> {
                    log.warn("Room already exists: {}", room.getNumber());
                    throw new RoomAlreadyExistsException(room.getNumber());
                });
    }
}
