package ru.senla.hotel.management;

import ru.senla.hotel.config.ApplicationConfig;
import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.exception.room.RoomOccupiedException;
import ru.senla.hotel.model.*;
import ru.senla.hotel.storage.AppState;
import ru.senla.hotel.storage.FileAppStateRepository;

import java.time.LocalDate;

@Component
public class Administrator {
    @Inject
    private ApplicationConfig config;

    @Inject
    private RoomManager roomManager;

    @Inject
    private ServiceManager serviceManager;

    @Inject
    private GuestManager guestManager;

    @Inject
    private BookingManager bookingManager;

    @Inject
    private ReportManager reportManager;

    @Inject
    private FileAppStateRepository appStateRepository;

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
        Room room = roomManager.getRoomByNumber(roomNumber);

        if (!bookingManager.isRoomFreeNow(room)) {
            throw new RoomOccupiedException(roomNumber);
        }

        roomManager.setRoomMaintenance(roomNumber, status);
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

    public void loadAppState() {
        AppState state = appStateRepository.load();

        roomManager.importStateFromAppState(state.getRooms());
        serviceManager.importStateFromAppState(state.getServices());
        guestManager.importStateFromAppState(state.getGuests());
        bookingManager.importStateFromAppState(state.getBookings());
    }

    public void saveAppState() {
        AppState state = new AppState();

        state.setRooms(roomManager.exportStateForAppState());
        state.setGuests(guestManager.exportStateForAppState());
        state.setServices(serviceManager.exportStateForAppState());
        state.setBookings(bookingManager.exportStateForAppState());

        appStateRepository.save(state);
    }
}
