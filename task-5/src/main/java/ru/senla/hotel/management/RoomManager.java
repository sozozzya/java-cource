package ru.senla.hotel.management;

import ru.senla.hotel.model.Room;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class RoomManager {
    private final List<Room> rooms = new ArrayList<>();

    public void addRoom(Room room) {
        boolean exists = rooms.stream()
                .anyMatch(r -> r.getNumber() == room.getNumber());

        if (exists) {
            System.out.println("Room " + room.getNumber() + " already exists.");
            return;
        }

        rooms.add(room);
        System.out.println("New room added: " + room);
    }

    public Optional<Room> findRoomOptional(int roomNumber) {
        return rooms.stream()
                .filter(r -> r.getNumber() == roomNumber)
                .findFirst();
    }

    public List<Room> getAllRooms() {
        return Collections.unmodifiableList(rooms);
    }

    public List<Room> getAvailableRooms() {
        return rooms.stream()
                .filter(r -> !r.isOccupied() && !r.isUnderMaintenance())
                .toList();
    }

    public long countAvailableRooms() {
        return rooms.stream().filter(r -> !r.isOccupied() && !r.isUnderMaintenance()).count();
    }

    public void setRoomMaintenance(int roomNumber, boolean status) {
        findRoomOptional(roomNumber).ifPresentOrElse(room -> {
            if (status) {
                if (room.isOccupied()) {
                    System.out.println("Cannot put room " + roomNumber + " under maintenance (occupied).");
                } else if (room.isUnderMaintenance()) {
                    System.out.println("Room " + roomNumber + " is already under maintenance.");
                } else {
                    room.setUnderMaintenance(true);
                    System.out.println("Room " + roomNumber + " is temporarily closed for maintenance.");
                }
            } else {
                if (!room.isUnderMaintenance()) {
                    System.out.println("Room " + roomNumber + " is not under maintenance.");
                } else {
                    room.setUnderMaintenance(false);
                    System.out.println("Room " + roomNumber + " is now available for guests.");
                }
            }
        }, () -> System.out.println("Room " + roomNumber + " not found."));
    }
}
