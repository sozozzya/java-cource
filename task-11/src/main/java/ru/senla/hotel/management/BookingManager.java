package ru.senla.hotel.management;

import ru.senla.hotel.config.ApplicationConfig;
import ru.senla.hotel.dao.BookingDAO;
import ru.senla.hotel.dao.GuestDAO;
import ru.senla.hotel.dao.RoomDAO;
import ru.senla.hotel.dao.ServiceDAO;
import ru.senla.hotel.db.ConnectionManager;
import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.exception.DAOException;
import ru.senla.hotel.exception.booking.*;
import ru.senla.hotel.exception.guest.GuestCsvException;
import ru.senla.hotel.model.Booking;
import ru.senla.hotel.model.Guest;
import ru.senla.hotel.model.Room;
import ru.senla.hotel.model.Service;

import java.io.*;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.*;

@Component
public class BookingManager {

    @Inject
    private BookingDAO bookingDAO;

    @Inject
    private RoomDAO roomDAO;

    @Inject
    private GuestDAO guestDAO;

    @Inject
    private ServiceDAO serviceDAO;

    @Inject
    private RoomManager roomManager;

    @Inject
    private ServiceManager serviceManager;

    @Inject
    private GuestManager guestManager;

    @Inject
    private ConnectionManager connectionManager;

    @Inject
    private ApplicationConfig config;

    public List<Booking> getActiveBookings() {
        return bookingDAO.findActiveByDate(LocalDate.now());
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

    public boolean isRoomFreeNow(Room room) {
        return bookingDAO
                .findActiveByRoomId(room.getId(), LocalDate.now())
                .isEmpty();
    }

    public List<Room> getAvailableRooms() {
        return roomManager.getAllRooms().stream()
                .filter(r -> !r.isUnderMaintenance())
                .filter(this::isRoomFreeNow)
                .toList();
    }

    public long countAvailableRooms() {
        return getAvailableRooms().size();
    }

    public List<Service> getServicesByGuest(String guestName) {
        Guest guest = guestManager.getGuestByName(guestName);

        return bookingDAO.findByGuestId(guest.getId()).stream()
                .flatMap(b -> b.getServices().stream())
                .toList();
    }

    private Optional<Booking> findActiveBookingForGuest(String guestName) {
        Guest guest = guestManager.getGuestByName(guestName);

        return bookingDAO.findByGuestId(guest.getId()).stream()
                .filter(b ->
                        !b.getCheckOutDate().isBefore(LocalDate.now()) &&
                                !b.getCheckInDate().isAfter(LocalDate.now())
                )
                .findFirst();
    }

    public List<Room> getRoomsFreeByDate(LocalDate date) {
        return roomManager.getAllRooms().stream()
                .filter(room ->
                        bookingDAO.findAll().stream()
                                .noneMatch(b ->
                                        b.getRoom().equals(room)
                                                && !b.getCheckOutDate().isBefore(date)
                                )
                )
                .toList();
    }

    private void ensureNoBookingConflict(Booking booking) {
        boolean conflict = bookingDAO.findAll().stream()
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
    }

    public Booking addBooking(Booking booking) {
        if (booking.getGuest() == null || booking.getRoom() == null) {
            throw new BookingException("Booking must have guest and room.") {
            };
        }
        try {
            ensureNoBookingConflict(booking);
            Booking saved = bookingDAO.save(booking);
            connectionManager.commit();
            return saved;
        } catch (Exception e) {
            connectionManager.rollback();
            throw new BookingException("Failed to add booking", e);
        }
    }

    public void addBookingsBatch(List<Booking> bookings) {
        try {
            for (Booking booking : bookings) {
                if (booking.getGuest() == null || booking.getRoom() == null) {
                    throw new BookingException("Booking must have guest and room.");
                }
                ensureNoBookingConflict(booking);
                bookingDAO.save(booking);
            }
            connectionManager.commit();
        } catch (Exception e) {
            connectionManager.rollback();
            throw new BookingException("Failed to import bookings batch", e);
        }
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
        try {
            Booking booking = bookingDAO.findAll().stream()
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

            booking.forceCheckOut(today);
            bookingDAO.update(booking);

            booking.getRoom().addStayRecord(
                    booking.getGuest().getName(),
                    booking.getCheckInDate(),
                    today,
                    config.getRoomHistorySize()
            );

            connectionManager.commit();
        } catch (Exception e) {
            connectionManager.rollback();
            throw new BookingException("Failed to check out", e);
        }
    }

    public void assignServiceToGuest(String guestName,
                                     String serviceName,
                                     LocalDate date) {
        try {
            Booking booking = findActiveBookingForGuest(guestName)
                    .orElseThrow(() -> new NoActiveBookingException(guestName));

            Service baseService = serviceManager.getServiceByName(serviceName);

            Service datedService = new Service(
                    baseService.getName(),
                    baseService.getPrice(),
                    date
            );

            serviceManager.addService(datedService);
            bookingDAO.addServiceToBooking(booking.getId(), datedService.getId());

            connectionManager.commit();
        } catch (Exception e) {
            connectionManager.rollback();
            throw new BookingException("Failed to assign service to guest", e);
        }
    }

    public double calculateGuestRoomCost(String guestName) {
        Guest guest = guestManager.getGuestByName(guestName);
        return bookingDAO.findAll().stream()
                .filter(b -> b.getGuest().equals(guest))
                .mapToDouble(Booking::calculateTotalRoomCost)
                .sum();
    }

    public double calculateGuestServicesCost(String guestName) {
        Guest guest = guestManager.getGuestByName(guestName);
        return bookingDAO.findAll().stream()
                .filter(b -> b.getGuest().equals(guest))
                .mapToDouble(Booking::calculateTotalServicesCost)
                .sum();
    }

    public double calculateGuestTotalCost(String guestName) {
        return calculateGuestRoomCost(guestName)
                + calculateGuestServicesCost(guestName);
    }

    public void exportBookingToCSV(String path) {
        if (path == null || path.isBlank()) {
            throw new BookingCsvException("CSV export path cannot be empty.");
        }

        File file = new File(path);

        if (file.isDirectory()) {
            file = new File(file, "bookings.csv");
        } else {
            if (!file.getName().toLowerCase().endsWith(".csv")) {
                throw new BookingCsvException(
                        "Invalid file format. CSV file expected: " + file.getName()
                );
            }
        }

        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println("id;guestId;roomId;checkIn;checkOut;serviceIds");
            bookingDAO.findAll().forEach(g -> writer.println(g.toCsv()));
        } catch (IOException | DAOException e) {
            throw new BookingCsvException("Failed to export bookings: " + e.getMessage());
        }
    }

    public void importBookingFromCSV(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine();
            List<Booking> bookings = new ArrayList<>();

            String line;
            while ((line = br.readLine()) != null) {
                bookings.add(Booking.fromCsv(line));
            }

            addBookingsBatch(bookings);

            resolveRelationsForAll();
            updateRoomHistoryFromBookings();

        } catch (Exception e) {
            throw new BookingCsvException("Failed to import bookings: " + e.getMessage());
        }
    }

    public void updateRoomHistoryFromBookings() {
        int maxHistorySize = config.getRoomHistorySize();

        roomManager.getAllRooms()
                .forEach(r -> r.getStayHistory().clear());

        bookingDAO.findAll().stream()
                .filter(b -> b.getRoom() != null && b.getGuest() != null)
                .filter(b -> !b.getCheckOutDate().isAfter(LocalDate.now()))
                .sorted(Comparator.comparing(Booking::getCheckOutDate).reversed())
                .forEach(b ->
                        b.getRoom().addStayRecord(
                                b.getGuest().getName(),
                                b.getCheckInDate(),
                                b.getCheckOutDate(),
                                maxHistorySize
                        )
                );
    }

    private void resolveRelationsForAll() {
        bookingDAO.findAll().forEach(this::resolveRelationsForBooking);
    }

    private void resolveRelationsForBooking(Booking booking) {
        guestManager.findById(booking.getGuestId()).ifPresent(booking::attachGuest);
        roomManager.findById(booking.getRoomId()).ifPresent(booking::attachRoom);

        List<Service> services = booking.getServiceIds().stream()
                .map(serviceManager::findById)
                .flatMap(Optional::stream)
                .toList();
        booking.attachServices(services);
    }
}
