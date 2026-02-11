package ru.senla.hotel.management;

import ru.senla.hotel.config.ApplicationConfig;
import ru.senla.hotel.exception.booking.*;
import ru.senla.hotel.exception.guest.GuestCsvException;
import ru.senla.hotel.model.Booking;
import ru.senla.hotel.model.Guest;
import ru.senla.hotel.model.Room;
import ru.senla.hotel.model.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.*;

public class BookingManager extends AbstractManager<Booking> {
    private final RoomManager roomManager;
    private final ServiceManager serviceManager;
    private final GuestManager guestManager;

    private final ApplicationConfig config;

    public BookingManager(RoomManager roomManager,
                          ServiceManager serviceManager,
                          GuestManager guestManager,
                          ApplicationConfig config) {

        this.roomManager = roomManager;
        this.serviceManager = serviceManager;
        this.guestManager = guestManager;
        this.config = config;
    }

    public List<Booking> getActiveBookings() {
        LocalDate today = LocalDate.now();
        return storage.values().stream()
                .filter(b -> !b.getCheckOutDate().isBefore(today))
                .filter(b -> b.getGuest() != null && b.getRoom() != null)
                .toList();
    }

    public List<Guest> getCurrentGuests() {
        return getActiveBookings().stream()
                .map(Booking::getGuest)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }

    public long getCurrentGuestsCount() {
        return getCurrentGuests().size();
    }

    private boolean datesOverlap(
            LocalDate start1, LocalDate end1,
            LocalDate start2, LocalDate end2) {

        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    public void addBooking(Booking booking) {
        if (booking.getGuest() == null || booking.getRoom() == null) {
            throw new BookingException("Booking must have guest and room.") {
            };
        }
        boolean conflict = storage.values().stream()
                .filter(b -> b.getRoom() != null)
                .anyMatch(b ->
                        b.getRoom().equals(booking.getRoom()) &&
                                datesOverlap(
                                        b.getCheckInDate(), b.getCheckOutDate(),
                                        booking.getCheckInDate(), booking.getCheckOutDate()
                                )
                );
        if (conflict) {
            throw new BookingConflictException(
                    booking.getRoom().getNumber(),
                    booking.getCheckInDate(),
                    booking.getCheckOutDate()
            );
        }
        save(booking);
    }

    public boolean isRoomFreeNow(Room room) {
        LocalDate today = LocalDate.now();
        return storage.values().stream()
                .filter(b -> b.getRoom().equals(room))
                .noneMatch(b ->
                        !b.getCheckOutDate().isBefore(today) &&
                                !b.getCheckInDate().isAfter(today)
                );
    }

    public void checkIn(String guestName,
                        int roomNumber,
                        LocalDate checkIn,
                        LocalDate checkOut) {

        if (checkOut.isBefore(checkIn)) {
            throw new InvalidBookingDatesException(checkIn, checkOut);
        }

        Guest guest = guestManager.getGuestByName(guestName);
        Room room = roomManager.getRoomByNumber(roomNumber);

        if (room.isUnderMaintenance()) {
            throw new RoomUnavailableException(roomNumber, "under maintenance");
        }
        if (!isRoomFreeNow(room)) {
            throw new RoomUnavailableException(roomNumber, "already occupied");
        }

        addBooking(new Booking(guest, room, checkIn, checkOut));
    }

    public void checkOut(int roomNumber) {
        LocalDate today = LocalDate.now();

        Booking booking = storage.values().stream()
                .filter(b -> b.getRoom().getNumber() == roomNumber)
                .filter(b ->
                        !b.getCheckOutDate().isBefore(today) &&
                                !b.getCheckInDate().isAfter(today)
                )
                .findFirst()
                .orElseThrow(() ->
                        new BookingNotFoundException(
                                "No active booking for room " + roomNumber
                        )
                );

        booking.getRoom().addStayRecord(
                booking.getGuest().getName(),
                booking.getCheckInDate(),
                today,
                config.getRoomHistorySize()
        );

        booking.forceCheckOut(today);
    }

    public void assignServiceToGuest(String guestName,
                                     String serviceName,
                                     LocalDate date) {
        Booking booking = findActiveBookingForGuest(guestName)
                .orElseThrow(() -> new NoActiveBookingException(guestName));

        Service baseService = serviceManager.getServiceByName(serviceName);

        Service datedService = new Service(
                baseService.getName(),
                baseService.getPrice(),
                date
        );

        serviceManager.save(datedService);
        booking.addService(datedService);
    }

    public double calculateGuestRoomCost(String guestName) {
        Guest guest = guestManager.getGuestByName(guestName);
        return storage.values().stream()
                .filter(b -> b.getGuest().equals(guest))
                .mapToDouble(Booking::calculateTotalRoomCost)
                .sum();
    }

    public double calculateGuestServicesCost(String guestName) {
        Guest guest = guestManager.getGuestByName(guestName);
        return storage.values().stream()
                .filter(b -> b.getGuest().equals(guest))
                .mapToDouble(Booking::calculateTotalServicesCost)
                .sum();
    }

    public double calculateGuestTotalCost(String guestName) {
        return calculateGuestRoomCost(guestName) + calculateGuestServicesCost(guestName);
    }

    public List<Service> getServicesByGuest(String guestName) {
        Guest guest = guestManager.getGuestByName(guestName);
        return storage.values().stream()
                .filter(b -> b.getGuest().equals(guest))
                .flatMap(b -> b.getServices().stream())
                .toList();
    }

    private Optional<Booking> findActiveBookingForGuest(String guestName) {
        Guest guest = guestManager.getGuestByName(guestName);
        return getActiveBookings().stream()
                .filter(b -> b.getGuest().equals(guest))
                .findFirst();
    }

    public List<Room> getRoomsFreeByDate(LocalDate date) {
        return roomManager.getAll().stream()
                .filter(room -> storage.values().stream()
                        .noneMatch(b -> b.getRoom().equals(room)
                                && !b.getCheckOutDate().isBefore(date)))
                .toList();
    }

    private void addHistoryIfFinished(Booking booking) {
        if (booking.getRoom() == null || booking.getGuest() == null) return;

        if (booking.getCheckOutDate().isBefore(LocalDate.now())) {
            booking.getRoom().addStayRecord(
                    booking.getGuest().getName(),
                    booking.getCheckInDate(),
                    booking.getCheckOutDate(),
                    config.getRoomHistorySize()
            );
        }
    }

    public void exportBookingToCSV(String path) {
        if (path == null || path.isBlank()) {
            throw new GuestCsvException("CSV export path cannot be empty.");
        }

        File file = new File(path);

        if (file.isDirectory()) {
            file = new File(file, "bookings.csv");
        } else {
            if (!file.getName().toLowerCase().endsWith(".csv")) {
                throw new GuestCsvException(
                        "Invalid file format. CSV file expected: " + file.getName()
                );
            }
        }

        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println("id;guestId;roomId;checkIn;checkOut;serviceIds");
            storage.values().forEach(b -> writer.println(b.toCsv()));
        } catch (Exception e) {
            throw new BookingCsvException("Failed to export bookings: " + e.getMessage());
        }
    }

    public void importBookingFromCSV(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                Booking importedBooking = Booking.fromCsv(line);
                save(importedBooking);
            }
            resolveRelationsForAll();
        } catch (Exception e) {
            throw new BookingCsvException("Failed to import bookings: " + e.getMessage());
        }
    }

    public List<Booking> exportStateForAppState() {
        return exportState();
    }

    public void importStateFromAppState(List<Booking> bookings) {
        importState(bookings);
        resolveRelationsForAll();
    }

    private void resolveRelationsForAll() {
        storage.values().forEach(this::resolveRelationsForBooking);
    }

    private void resolveRelationsForBooking(Booking booking) {
        guestManager.findById(booking.getGuestId()).ifPresent(booking::attachGuest);
        roomManager.findById(booking.getRoomId()).ifPresent(booking::attachRoom);

        List<Service> services = booking.getServiceIds().stream()
                .map(serviceManager::findById)
                .flatMap(Optional::stream)
                .toList();
        booking.attachServices(services);

        addHistoryIfFinished(booking);
    }
}
