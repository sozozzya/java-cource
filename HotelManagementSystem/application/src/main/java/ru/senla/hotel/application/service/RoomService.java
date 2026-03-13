package ru.senla.hotel.application.service;

import ru.senla.hotel.domain.entity.Room;
import ru.senla.hotel.domain.enums.SortField;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface RoomService {

    Optional<Room> findById(Long id);

    Room getRoomByNumber(int number);

    List<Room> getAllRooms(SortField field);

    Room addRoom(Room room);

    void addRoomsBatch(List<Room> rooms);

    void changeRoomPrice(int roomNumber, BigDecimal newPrice);

    boolean isRoomStatusChangeEnabled();

    void changeMaintenanceStatus(int roomNumber, boolean status);

    void exportRoomToCSV(String path);

    void importRoomFromCSV(String path);
}
