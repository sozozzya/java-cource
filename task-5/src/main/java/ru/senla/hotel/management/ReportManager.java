package ru.senla.hotel.management;

import ru.senla.hotel.model.Guest;
import ru.senla.hotel.model.Room;
import ru.senla.hotel.model.Service;

import java.time.LocalDate;
import java.util.*;

public class ReportManager {
    private final BookingManager bookingManager;
    private final RoomManager roomManager;
    private final ServiceManager serviceManager;

    public ReportManager(BookingManager bookingManager, ServiceManager serviceManager, RoomManager roomManager) {
        this.bookingManager = bookingManager;
        this.serviceManager = serviceManager;
        this.roomManager = roomManager;
    }

    public void printRooms(Comparator<Room> comparator) {
        System.out.println("===== ROOM LIST =====");
        roomManager.getAllRooms().stream().sorted(comparator).forEach(System.out::println);
    }

    public void printAvailableRooms(Comparator<Room> comparator) {
        System.out.println("===== AVAILABLE ROOMS =====");
        roomManager.getAvailableRooms().stream().sorted(comparator).forEach(System.out::println);
    }

    public void printAvailableRoomsCount() {
        System.out.println("Free rooms: " + roomManager.countAvailableRooms());
    }

    public void printRoomsFreeByDate(LocalDate date) {
        System.out.println("===== ROOMS FREE BY " + date + " =====");
        bookingManager.getRoomsFreeByDate(date).forEach(System.out::println);
    }

    public void printRoomDetails(int roomNumber) {
        roomManager.findRoomOptional(roomNumber).ifPresentOrElse(room -> {
            System.out.println("===== ROOM DETAILS =====");
            System.out.println(room);
            System.out.println("Recent guests:");
            room.getStayHistory().forEach(System.out::println);
        }, () -> System.out.println("Room " + roomNumber + " not found."));
    }

    public void printRoomHistory(int roomNumber) {
        roomManager.findRoomOptional(roomNumber).ifPresentOrElse(room -> {
            System.out.println("Last 3 guests of room " + roomNumber + ":");
            room.getStayHistory().forEach(System.out::println);
        }, () -> System.out.println("Room " + roomNumber + " not found."));
    }

    public void printAllPrices() {
        System.out.println("===== PRICES =====");

        System.out.println("--- Rooms ---");
        roomManager.getAllRooms().stream()
                .sorted(SortUtils.byRoomPrice())
                .forEach(r -> System.out.println("Room " + r.getNumber() + ": " + r.getPricePerNight() + "₽"));

        System.out.println("\n--- Services ---");
        serviceManager.getAvailableServices().stream()
                .sorted(SortUtils.byServicePrice())
                .forEach(s -> System.out.println(s.getName() + ": " + s.getPrice() + "₽"));
    }

    public void printGuests(Comparator<Guest> comparator) {
        System.out.println("===== GUESTS =====");
        bookingManager.getCurrentGuests().stream().sorted(comparator).forEach(System.out::println);
    }

    public void printGuestCount() {
        System.out.println("Current guests: " + bookingManager.getCurrentGuestsCount());
    }

    public void printGuestBill(Guest guest) {
        double totalRoom = bookingManager.calculateGuestRoomCost(guest);
        double totalServices = bookingManager.calculateGuestServicesCost(guest);
        double total = bookingManager.calculateGuestTotalCost(guest);

        System.out.println("===== BILL FOR " + guest.getName() + " =====");
        System.out.println("Room cost: " + totalRoom + "₽");
        System.out.println("Services:  " + totalServices + "₽");
        System.out.println("Total due: " + total + "₽");
    }

    public void printGuestServices(Guest guest, Comparator<Service> comparator) {
        System.out.println("===== SERVICES USED BY " + guest.getName() + " =====");
        bookingManager.getServicesByGuest(guest).stream().sorted(comparator).forEach(System.out::println);
    }
}
