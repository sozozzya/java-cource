package ru.senla.hotel.management;

import ru.senla.hotel.config.ApplicationConfig;
import ru.senla.hotel.config.ConfigLoader;
import ru.senla.hotel.model.*;

import java.time.LocalDate;

public class Administrator {
    ApplicationConfig config = ConfigLoader.load("application.properties");

    private final RoomManager roomManager = new RoomManager(config);
    private final ServiceManager serviceManager = new ServiceManager();
    private final GuestManager guestManager = new GuestManager();
    private final BookingManager bookingManager = new BookingManager(
            roomManager,
            serviceManager,
            guestManager,
            config
    );
    private final ReportManager reportManager = new ReportManager(
            bookingManager,
            serviceManager,
            roomManager);

    public void addRoom(Room room) {
        roomManager.save(room);
    }

    public void addService(Service service) {
        serviceManager.save(service);
    }

    public void addGuest(Guest guest) {
        guestManager.save(guest);
    }

    public void setRoomMaintenance(int roomNumber, boolean status) {
        roomManager.setRoomMaintenance(roomNumber, status, bookingManager);
    }

    public void changeRoomPrice(int roomNumber, double newPrice) {
        roomManager.changeRoomPrice(roomNumber, newPrice);
    }

    public void changeServicePrice(String serviceName, double newPrice) {
        serviceManager.changeServicePrice(serviceName, newPrice);
    }

    public void checkIn(String guestName, int roomNumber, LocalDate checkIn, LocalDate checkOut) {
        bookingManager.checkIn(guestName, roomNumber, checkIn, checkOut);
    }

    public void checkOut(int roomNumber) {
        bookingManager.checkOut(roomNumber);
    }

    public void assignServiceToGuest(String guestName, String serviceName, LocalDate date) {
        bookingManager.assignServiceToGuest(guestName, serviceName, date);
    }

    public void printAllRoomsByPrice() {
        reportManager.printAllRooms(SortUtils.byRoomPrice());
    }

    public void printAllRoomsByCapacity() {
        reportManager.printAllRooms(SortUtils.byRoomCapacity());
    }

    public void printAllRoomsByStars() {
        reportManager.printAllRooms(SortUtils.byRoomStars());
    }

    public void printAvailableRoomsByPrice() {
        reportManager.printAvailableRooms(SortUtils.byRoomPrice());
    }

    public void printAvailableRoomsByCapacity() {
        reportManager.printAvailableRooms(SortUtils.byRoomCapacity());
    }

    public void printAvailableRoomsByStars() {
        reportManager.printAvailableRooms(SortUtils.byRoomStars());
    }

    public void printAllServices() {
        reportManager.printAllServices();
    }

    public void printGuestsByName() {
        reportManager.printCurrentGuests(SortUtils.byGuestName());
    }

    public void printGuestsByCheckoutDate() {
        reportManager.printCurrentGuests(SortUtils.byGuestCheckOutDate());
    }

    public void printAvailableRoomCount() {
        reportManager.printAvailableRoomsCount();
    }

    public void printGuestCount() {
        reportManager.printGuestCount();
    }

    public void printAllPrices() {
        reportManager.printAllPrices();
    }

    public void printGuestBill(String guestName) {
        reportManager.printGuestBill(guestName);
    }

    public void printGuestServicesByPrice(String guestName) {
        reportManager.printGuestServices(guestName, SortUtils.byServicePrice());
    }

    public void printGuestServicesByDate(String guestName) {
        reportManager.printGuestServices(guestName, SortUtils.byServiceDate());
    }

    public void printRoomDetails(int roomNumber) {
        reportManager.printRoomDetails(roomNumber);
    }

    public void printRoomsFreeByDate(LocalDate date) {
        reportManager.printRoomsFreeByDate(date);
    }

    public void printRoomHistory(int roomNumber) {
        reportManager.printRoomHistory(roomNumber);
    }

    public void exportRooms(String path) {
        roomManager.exportRoomToCSV(path);
    }

    public void importRooms(String path) {
        roomManager.importRoomFromCSV(path);
    }

    public void exportServices(String path) {
        serviceManager.exportServiceToCSV(path);
    }

    public void importServices(String path) {
        serviceManager.importServiceFromCSV(path);
    }

    public void exportGuests(String path) {
        guestManager.exportGuestToCSV(path);
    }

    public void importGuests(String path) {
        guestManager.importGuestFromCSV(path);
    }

    public void exportBookings(String path) {
        bookingManager.exportBookingToCSV(path);
    }

    public void importBookings(String path) {
        bookingManager.importBookingFromCSV(path);
    }

    public boolean isRoomStatusChangeEnabled() {
        return config.isRoomStatusChangeEnabled();
    }
}
