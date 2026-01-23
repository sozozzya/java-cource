package ru.senla.task4.hotel.management;

import ru.senla.task4.hotel.model.*;

import java.time.LocalDate;

public class Administrator {
    private final RoomManager roomManager = new RoomManager();
    private final ServiceManager serviceManager = new ServiceManager();
    private final BookingManager bookingManager = new BookingManager(roomManager, serviceManager);
    private final PriceManager priceManager = new PriceManager();
    private final ReportManager reportManager = new ReportManager(bookingManager, serviceManager, roomManager);

    public void addRoom(Room room) {
        roomManager.addRoom(room);
    }

    public void addService(Service service) {
        serviceManager.addService(service);
    }

    public void checkIn(Guest guest, int roomNumber, LocalDate checkIn, LocalDate checkOut) {
        bookingManager.checkIn(guest, roomNumber, checkIn, checkOut);
    }

    public void checkOut(int roomNumber) {
        bookingManager.checkOut(roomNumber);
    }

    public void assignServiceToGuest(Guest guest, String serviceName, LocalDate date) {
        bookingManager.assignServiceToGuest(guest, serviceName, date);
    }

    public void setRoomMaintenance(int roomNumber, boolean status) {
        roomManager.setRoomMaintenance(roomNumber, status);
    }

    public void changeRoomPrice(int roomNumber, double newPrice) {
        Room room = roomManager.findRoom(roomNumber);
        if (room != null) {
            priceManager.changeRoomPrice(room, newPrice);
        } else {
            System.out.println("Room " + roomNumber + " not found.");
        }
    }

    public void changeServicePrice(String serviceName, double newPrice) {
        Service service = serviceManager.findServiceByName(serviceName);
        if (service != null) {
            priceManager.changeServicePrice(service, newPrice);
        } else {
            System.out.println("Service " + serviceName + " not found.");
        }
    }

    public void printAllRoomsByPrice() {
        reportManager.printRooms(SortUtils.byRoomPrice());
    }

    public void printAllRoomsByCapacity() {
        reportManager.printRooms(SortUtils.byRoomCapacity());
    }

    public void printAllRoomsByStars() {
        reportManager.printRooms(SortUtils.byRoomStars());
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

    public void printGuestsByName() {
        reportManager.printGuests(SortUtils.byGuestName());
    }

    public void printGuestsByCheckoutDate() {
        reportManager.printGuests(SortUtils.byGuestCheckOutDate());
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

    public void printGuestBill(Guest guest) {
        reportManager.printGuestBill(guest);
    }

    public void printGuestServicesByPrice(Guest guest) {
        reportManager.printGuestServices(guest, SortUtils.byServicePrice());
    }

    public void printGuestServicesByDate(Guest guest) {
        reportManager.printGuestServices(guest, SortUtils.byServiceDate());
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
}
