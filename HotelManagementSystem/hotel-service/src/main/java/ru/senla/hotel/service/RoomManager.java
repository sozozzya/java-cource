package ru.senla.hotel.service;

import ru.senla.hotel.domain.model.Room;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface RoomManager {

    Optional<Room> findById(Long id);

    Room getRoomByNumber(int number);

    List<Room> getAllRooms();

    Room addRoom(Room room);

    void addRoomsBatch(List<Room> rooms);

    void changeRoomPrice(int roomNumber, BigDecimal newPrice);

    public boolean isRoomStatusChangeEnabled();

    void changeMaintenanceStatus(int roomNumber, boolean status);

    void exportRoomToCSV(String path);

    void importRoomFromCSV(String path);
}
