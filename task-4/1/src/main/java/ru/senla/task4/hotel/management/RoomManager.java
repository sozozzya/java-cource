package ru.senla.task4.hotel.management;

import ru.senla.task4.hotel.model.Room;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RoomManager {
    private final List<Room> rooms = new ArrayList<>();

    public void addRoom(Room room) {
        rooms.add(room);
        System.out.println("New room added: " + room);
    }

    public void removeRoom(int roomNumber) {
        rooms.removeIf(r -> r.getNumber() == roomNumber);
        System.out.println("Room " + roomNumber + " removed.");
    }

    public Room findRoom(int roomNumber) {
        return rooms.stream()
                .filter(r -> r.getNumber() == roomNumber)
                .findFirst()
                .orElse(null);
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
        Room room = findRoom(roomNumber);
        if (room == null) {
            System.out.println("Room " + roomNumber + " not found.");
            return;
        }

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
    }
}
