package ru.senla.task3.hotel.management;

import ru.senla.task3.hotel.model.Room;
import ru.senla.task3.hotel.model.Service;

import java.util.ArrayList;
import java.util.List;

public class Administrator {
    private final List<Room> rooms = new ArrayList<>();
    private final List<Service> services = new ArrayList<>();
    private final PriceManager priceManager = new PriceManager();

    public void addRoom(Room room) {
        rooms.add(room);
        System.out.println("New room added: " + room + "\n");
    }

    public void addService(Service service) {
        services.add(service);
        System.out.println("New service added: " + service + "\n");
    }

    public void checkIn(int roomNumber) {
        Room room = findRoom(roomNumber);
        if (room != null) {
            room.occupy();
        }
    }

    public void checkOut(int roomNumber) {
        Room room = findRoom(roomNumber);
        if (room != null) {
            room.vacate();
        }
    }

    public void setRoomMaintenance(int roomNumber, boolean status) {
        Room room = findRoom(roomNumber);
        if (room == null) return;

        if (status) {
            if (room.isOccupied()) {
                System.out.println("Cannot put room " + roomNumber + " under maintenance. It is currently occupied by a guest.\n");
            } else if (room.isUnderMaintenance()) {
                System.out.println("Room " + roomNumber + " is already under maintenance.\n");
            } else {
                room.setUnderMaintenance(true);
                System.out.println("Room " + roomNumber + " is temporarily closed for maintenance.\n");
            }
        } else {
            if (!room.isUnderMaintenance()) {
                System.out.println("Room " + roomNumber + " is not under maintenance.\n");
            } else {
                room.setUnderMaintenance(false);
                System.out.println("Room " + roomNumber + " is now available for guests.\n");
            }
        }
    }

    public void changeRoomPrice(int roomNumber, double newPrice) {
        Room room = findRoom(roomNumber);
        if (room != null) {
            priceManager.changeRoomPrice(room, newPrice);
        }
    }

    public void changeServicePrice(String serviceName, double newPrice) {
        Service service = findService(serviceName);
        if (service != null) {
            priceManager.changeServicePrice(service, newPrice);
        }
    }

    private Room findRoom(int number) {
        for (Room r : rooms) {
            if (r.getNumber() == number) return r;
        }
        System.out.println("Room " + number + " not found.\n");
        return null;
    }

    private Service findService(String name) {
        for (Service s : services) {
            if (s.getName().equalsIgnoreCase(name)) return s;
        }
        System.out.println("Service \"" + name + "\" not found.\n");
        return null;
    }

    public void printAllRooms() {
        System.out.println("Rooms list:");
        for (Room r : rooms) System.out.println("   " + r);
    }

    public void printAllServices() {
        System.out.println("Services list:");
        for (Service s : services) System.out.println("   " + s);
    }
}
