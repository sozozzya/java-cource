package ru.senla.hotel.management;

import ru.senla.hotel.config.ApplicationConfig;
import ru.senla.hotel.exception.FeatureDisabledException;
import ru.senla.hotel.exception.guest.GuestCsvException;
import ru.senla.hotel.exception.room.*;
import ru.senla.hotel.model.Room;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.*;

public class RoomManager extends AbstractManager<Room> {
    private final ApplicationConfig config;

    public RoomManager(ApplicationConfig config) {
        this.config = config;
    }

    public void changeRoomPrice(int roomNumber, double newPrice) {
        if (newPrice < 0) {
            throw new InvalidRoomPriceException(newPrice);
        }
        Room room = getRoomByNumber(roomNumber);
        room.setPricePerNight(newPrice);
    }

    public Optional<Room> findRoomByNumber(int roomNumber) {
        return storage.values().stream()
                .filter(r -> r.getNumber() == roomNumber)
                .findFirst();
    }

    public Room getRoomByNumber(int roomNumber) {
        return findRoomByNumber(roomNumber)
                .orElseThrow(() -> new RoomNotFoundException(roomNumber));
    }

    public List<Room> getAvailableRooms(BookingManager bookingManager) {
        return storage.values().stream()
                .filter(r -> !r.isUnderMaintenance())
                .filter(bookingManager::isRoomFreeNow)
                .toList();
    }

    public long countAvailableRooms(BookingManager bookingManager) {
        return getAvailableRooms(bookingManager).size();
    }

    public void addRoom(Room room) {
        boolean exists = storage.values().stream()
                .anyMatch(r -> r.getNumber() == room.getNumber());

        if (exists) {
            throw new RoomAlreadyExistsException(room.getNumber());
        }

        save(room);
    }

    public void setRoomMaintenance(int roomNumber, boolean status, BookingManager bookingManager) {
        if (!config.isRoomStatusChangeEnabled()) {
            throw new FeatureDisabledException("Changing room maintenance status is disabled by configuration.");
        }

        Room room = getRoomByNumber(roomNumber);

        if (!bookingManager.isRoomFreeNow(room)) {
            throw new RoomOccupiedException(roomNumber);
        }

        if (room.isUnderMaintenance() == status) {
            throw new RoomMaintenanceException(
                    "Room " + roomNumber +
                            (status
                                    ? " is already under maintenance."
                                    : " is not under maintenance.")
            );
        }

        room.setUnderMaintenance(status);
    }

    public void exportRoomToCSV(String path) {
        if (path == null || path.isBlank()) {
            throw new GuestCsvException("CSV export path cannot be empty.");
        }

        File file = new File(path);

        if (file.isDirectory()) {
            file = new File(file, "rooms.csv");
        } else {
            if (!file.getName().toLowerCase().endsWith(".csv")) {
                throw new GuestCsvException(
                        "Invalid file format. CSV file expected: " + file.getName()
                );
            }
        }

        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println("id;number;capacity;stars;pricePerNight;isUnderMaintenance");
            storage.values().forEach(r -> writer.println(r.toCsv()));
        } catch (Exception e) {
            throw new RoomCsvException("Failed to export rooms: " + e.getMessage());
        }
    }

    public void importRoomFromCSV(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                Room importedRoom = Room.fromCsv(line);
                addRoom(importedRoom);
            }
        } catch (Exception e) {
            throw new RoomCsvException("Failed to import rooms: " + e.getMessage());
        }
    }

    public List<Room> exportStateForAppState() {
        return exportState();
    }

    public void importStateFromAppState(List<Room> rooms) {
        importState(rooms);
    }
}
