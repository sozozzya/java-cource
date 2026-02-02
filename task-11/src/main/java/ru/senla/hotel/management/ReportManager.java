package ru.senla.hotel.management;

import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.model.Booking;
import ru.senla.hotel.model.Room;
import ru.senla.hotel.model.Service;

import java.time.LocalDate;
import java.util.*;

@Component
public class ReportManager {
    @Inject
    private BookingManager bookingManager;

    @Inject
    private RoomManager roomManager;

    @Inject
    private ServiceManager serviceManager;

    public void printAllRooms(Comparator<Room> comparator) {
        System.out.println("===== ROOM LIST =====");
        roomManager.getAllRooms().stream().sorted(comparator).forEach(System.out::println);
    }

    public void printAvailableRooms(Comparator<Room> comparator) {
        System.out.println("===== AVAILABLE ROOMS =====");
        bookingManager.getAvailableRooms()
                .stream()
                .sorted(comparator)
                .forEach(System.out::println);
    }

    public void printAvailableRoomsCount() {
        System.out.println("Free rooms: " + bookingManager.countAvailableRooms());
    }

    public void printRoomsFreeByDate(LocalDate date) {
        System.out.println("===== ROOMS FREE BY " + date + " =====");
        bookingManager.getRoomsFreeByDate(date).forEach(System.out::println);
    }

    public void printRoomDetails(int roomNumber) {
        System.out.println("===== ROOM DETAILS =====");
        Room room = roomManager.getRoomByNumber(roomNumber);
        System.out.println(room);
        System.out.println("Recent guests:");
        room.getStayHistory().forEach(System.out::println);
    }

    public void printRoomHistory(int roomNumber) {
        System.out.println("Last 3 guests of room " + roomNumber + ":");
        Room room = roomManager.getRoomByNumber(roomNumber);
        room.getStayHistory().forEach(System.out::println);
    }

    public void printAllServices() {
        System.out.println("===== SERVICE LIST =====");
        serviceManager.getAvailableServices().forEach(System.out::println);
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

    public void printCurrentGuests(Comparator<Booking> comparator) {
        System.out.println("===== CURRENT GUESTS =====");
        bookingManager.getActiveBookings().stream()
                .sorted(comparator)
                .forEach(b -> System.out.println(
                        b.getGuest().getName() +
                                " | Room " + b.getRoom().getNumber() +
                                " | Check-out: " + b.getCheckOutDate()
                ));
    }

    public void printGuestCount() {
        System.out.println("Current guests: " + bookingManager.getCurrentGuestsCount());
    }

    public void printGuestBill(String guestName) {
        double roomCost = bookingManager.calculateGuestRoomCost(guestName);
        double serviceCost = bookingManager.calculateGuestServicesCost(guestName);
        double total = bookingManager.calculateGuestTotalCost(guestName);

        System.out.println("===== BILL FOR " + guestName + " =====");
        System.out.println("Room cost: " + roomCost + "₽");
        System.out.println("Services:  " + serviceCost + "₽");
        System.out.println("Total due: " + total + "₽");
    }

    public void printGuestServices(String guestName,
                                   Comparator<Service> comparator) {
        System.out.println("===== SERVICES USED BY " + guestName + " =====");
        bookingManager.getServicesByGuest(guestName).stream()
                .sorted(comparator)
                .forEach(System.out::println);
    }
}
