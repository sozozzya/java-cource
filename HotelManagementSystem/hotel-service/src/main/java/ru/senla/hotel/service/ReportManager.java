package ru.senla.hotel.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.senla.hotel.ApplicationConfig;
import ru.senla.hotel.dao.BookingDAO;
import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.model.Booking;
import ru.senla.hotel.model.BookingService;
import ru.senla.hotel.model.Room;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;

@Component
public class ReportManager {
    @Inject
    private BookingManager bookingManager;

    @Inject
    private RoomManager roomManager;

    @Inject
    private ServiceManager serviceManager;

    @Inject
    private BookingServiceManager bookingServiceManager;

    @Inject
    private BookingDAO bookingDAO;

    @Inject
    private ApplicationConfig config;

    private static final Logger log = LoggerFactory.getLogger(ReportManager.class);

    public void printAllRooms(Comparator<Room> comparator) {
        log.info("Printing all rooms");

        System.out.println("===== ROOM LIST =====");
        roomManager.getAllRooms().stream().sorted(comparator).forEach(System.out::println);

        log.info("All rooms printed successfully");
    }

    public void printAvailableRooms(Comparator<Room> comparator) {
        log.info("Printing available rooms");

        System.out.println("===== AVAILABLE ROOMS =====");
        bookingManager.getAvailableRooms()
                .stream()
                .sorted(comparator)
                .forEach(System.out::println);

        log.info("Available rooms printed successfully");
    }

    public void printAvailableRoomsCount() {
        log.info("Printing available rooms count");

        System.out.println("Free rooms: " + bookingManager.countAvailableRooms());

        log.info("Available rooms count printed successfully");
    }

    public void printRoomsFreeByDate(LocalDate date) {
        log.info("Printing rooms free by date={}", date);

        System.out.println("===== ROOMS FREE BY " + date + " =====");
        bookingManager.getRoomsFreeByDate(date).forEach(System.out::println);

        log.info("Rooms free by date printed successfully");
    }

    public void printRoomDetails(int roomNumber) {
        log.info("Printing room details, roomNumber={}", roomNumber);

        System.out.println("===== ROOM DETAILS =====");
        Room room = roomManager.getRoomByNumber(roomNumber);
        System.out.println(room);
        System.out.println("Recent guests:");
        bookingManager.getRoomHistory(roomNumber)
                .forEach(System.out::println);

        log.info("Room details printed successfully, roomNumber={}", roomNumber);
    }

    public void printRoomHistory(int roomNumber) {
        log.info("Printing room history, roomNumber={}", roomNumber);

        System.out.println("Recent guests of room " + roomNumber + ":");
        bookingManager.getRoomHistory(roomNumber)
                .forEach(System.out::println);

        log.info("Room history printed successfully, roomNumber={}", roomNumber);
    }

    public void printAllServices() {
        log.info("Printing all services");

        System.out.println("===== SERVICE LIST =====");
        serviceManager.getAvailableServices().forEach(System.out::println);

        log.info("All services printed successfully");
    }

    public void printAllPrices() {
        log.info("Printing all prices");

        System.out.println("===== PRICES =====");

        System.out.println("--- Rooms ---");
        roomManager.getAllRooms().stream()
                .sorted(SortUtils.byRoomPrice())
                .forEach(r -> System.out.println("Room " + r.getNumber() + ": " + r.getPricePerNight() + "₽"));

        System.out.println("\n--- Services ---");
        serviceManager.getAvailableServices()
                .forEach(s -> System.out.println(s.getName() + ": " + s.getPrice() + "₽"));

        log.info("All prices printed successfully");
    }

    public void printCurrentGuests(Comparator<Booking> comparator) {
        log.info("Printing current guests");

        System.out.println("===== CURRENT GUESTS =====");
        bookingManager.getActiveBookings().stream()
                .sorted(comparator)
                .forEach(b -> System.out.println(
                        b.getGuest().getName() +
                                " | Room " + b.getRoom().getNumber() +
                                " | Check-out: " + b.getCheckOutDate()
                ));

        log.info("Current guests printed successfully");
    }

    public void printGuestCount() {
        log.info("Printing guest count");

        System.out.println("Current guests: " + bookingManager.getCurrentGuestsCount());

        log.info("Guest count printed successfully");
    }

    public void printGuestBill(String guestName) {
        log.info("Printing bill for guest='{}'", guestName);

        BigDecimal roomCost = bookingManager.calculateGuestRoomCost(guestName);
        BigDecimal serviceCost = bookingManager.calculateGuestServicesCost(guestName);
        BigDecimal total = bookingManager.calculateGuestTotalCost(guestName);

        System.out.println("===== BILL FOR " + guestName + " =====");
        System.out.println("Room cost: " + roomCost + "₽");
        System.out.println("Services:  " + serviceCost + "₽");
        System.out.println("Total due: " + total + "₽");

        log.info("Bill printed successfully for guest='{}'", guestName);
    }

    public void printGuestServices(String guestName, Comparator<BookingService> comparator) {
        log.info("Printing services for guest='{}'", guestName);

        System.out.println("===== SERVICES USED BY " + guestName + " =====");
        bookingServiceManager.getServicesByGuest(guestName).stream()
                .sorted(comparator)
                .forEach(System.out::println);

        log.info("Guest services printed successfully for guest='{}'", guestName);
    }
}
