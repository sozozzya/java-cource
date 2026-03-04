package ru.senla.hotel.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.senla.hotel.ApplicationConfig;
import ru.senla.hotel.dao.BookingDAO;
import ru.senla.hotel.dao.GuestDAO;
import ru.senla.hotel.dao.RoomDAO;
import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.dao.exception.DAOException;
import ru.senla.hotel.model.Booking;
import ru.senla.hotel.model.Guest;
import ru.senla.hotel.model.Room;
import ru.senla.hotel.model.StayRecord;
import ru.senla.hotel.service.exception.booking.BookingException;
import ru.senla.hotel.service.exception.booking.BookingConflictException;
import ru.senla.hotel.service.exception.booking.BookingCsvException;
import ru.senla.hotel.service.exception.booking.RoomUnavailableException;
import ru.senla.hotel.service.exception.booking.BookingNotFoundException;
import ru.senla.hotel.service.exception.booking.InvalidBookingDatesException;
import ru.senla.hotel.service.exception.guest.GuestNotFoundException;
import ru.senla.hotel.service.exception.room.RoomNotFoundException;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class BookingManager {

    @Inject
    private BookingDAO bookingDAO;

    @Inject
    private RoomDAO roomDAO;

    @Inject
    private GuestDAO guestDAO;

    @Inject
    private RoomManager roomManager;

    @Inject
    private ServiceManager serviceManager;

    @Inject
    private GuestManager guestManager;

    @Inject
    private ApplicationConfig config;

    private static final Logger log = LoggerFactory.getLogger(BookingManager.class);

    public List<Booking> getActiveBookings() {
        log.info("Fetching active bookings for date={}", LocalDate.now());
        return bookingDAO.findActiveByDate(LocalDate.now());
    }

    public List<Guest> getCurrentGuests() {
        log.info("Fetching current guests");
        return getActiveBookings().stream()
                .map(Booking::getGuest)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }

    public long getCurrentGuestsCount() {
        return getCurrentGuests().size();
    }

    public boolean isRoomFree(Room room, LocalDate checkIn, LocalDate checkOut) {
        log.info("Checking room availability, roomNumber={}, period {} - {}",
                room.getNumber(), checkIn, checkOut);

        return bookingDAO.findBookingsByRoomAndPeriod(room.getId(), checkIn, checkOut).isEmpty();
    }

    public List<Room> getAvailableRooms() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        log.info("Fetching available rooms for today");

        return roomManager.getAllRooms().stream()
                .filter(r -> !r.isUnderMaintenance())
                .filter(r -> isRoomFree(r, today, tomorrow))
                .toList();
    }

    public long countAvailableRooms() {
        return getAvailableRooms().size();
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

    public List<StayRecord> getRoomHistory(int roomNumber) {

        Room room = roomManager.getRoomByNumber(roomNumber);

        return bookingDAO.findCompletedByRoomId(room.getId()).stream()
                .limit(config.getRoomHistorySize())
                .map(b -> new StayRecord(
                        b.getGuest().getName(),
                        b.getCheckInDate(),
                        b.getCheckOutDate()
                ))
                .toList();
    }

    private void ensureNoBookingConflict(Booking booking) {
        boolean conflict = !bookingDAO
                .findBookingsByRoomAndPeriod(
                        booking.getRoom().getId(),
                        booking.getCheckInDate(),
                        booking.getCheckOutDate()
                )
                .isEmpty();

        if (conflict) {
            log.error(
                    "Booking conflict detected: roomNumber={}, checkIn={}, checkOut={}",
                    booking.getRoom().getNumber(),
                    booking.getCheckInDate(),
                    booking.getCheckOutDate()
            );
            throw new BookingConflictException(
                    booking.getRoom().getNumber(),
                    booking.getCheckInDate(),
                    booking.getCheckOutDate()
            );
        }
    }

    public Booking addBooking(Booking booking) {
        log.info(
                "Adding booking: guestId={}, roomId={}, checkIn={}, checkOut={}",
                booking.getGuest() != null ? booking.getGuest().getId() : null,
                booking.getRoom() != null ? booking.getRoom().getId() : null,
                booking.getCheckInDate(),
                booking.getCheckOutDate()
        );

        if (booking.getGuest() == null || booking.getRoom() == null) {
            throw new BookingException("Booking must have guest and room.") {
            };
        }
        try {
            ensureNoBookingConflict(booking);
            Booking saved = bookingDAO.save(booking);

            log.info("Booking successfully added, bookingId={}", saved.getId());
            return saved;
        } catch (Exception e) {
            log.error("Failed to add booking", e);
            throw new BookingException("Failed to add booking", e);
        }
    }

    public void addBookingsBatch(List<Booking> bookings) {
        log.info("Importing bookings batch, size={}", bookings.size());

        try {
            for (Booking booking : bookings) {
                if (booking.getGuest() == null || booking.getRoom() == null) {
                    throw new BookingException("Booking must have guest and room.");
                }
                ensureNoBookingConflict(booking);
                bookingDAO.save(booking);
            }
            log.info("Bookings batch successfully imported, count={}", bookings.size());
        } catch (Exception e) {
            log.error("Failed to import bookings batch", e);
            throw new BookingException("Failed to import bookings batch", e);
        }
    }

    public void checkIn(String guestName,
                        int roomNumber,
                        LocalDate checkIn,
                        LocalDate checkOut) {
        log.info("Check-in request: guest='{}', roomNumber={}, checkIn={}, checkOut={}",
                guestName, roomNumber, checkIn, checkOut
        );

        if (checkOut.isBefore(checkIn)) {
            throw new InvalidBookingDatesException(checkIn, checkOut);
        }

        if (checkIn.isBefore(LocalDate.now())) {
            throw new InvalidBookingDatesException(checkIn, LocalDate.now());
        }

        try {
            Guest guest = guestManager.getGuestByName(guestName);
            if (guest == null) {
                throw new GuestNotFoundException(guestName);
            }

            Room room = roomManager.getRoomByNumber(roomNumber);
            if (room == null) {
                throw new RoomNotFoundException(roomNumber);
            }

            if (room.isUnderMaintenance()) {
                throw new RoomUnavailableException(roomNumber, "under maintenance");
            }

            if (!isRoomFree(room, checkIn, checkOut)) {
                throw new RoomUnavailableException(roomNumber, "already booked for these dates");
            }

            addBooking(new Booking(guest, room, checkIn, checkOut));
        } catch (Exception e) {
            log.error("Failed to check in guest='{}' to roomNumber={}",
                    guestName, roomNumber, e);
            throw new BookingException("Failed to check in guest", e);
        }
    }

    public void checkOut(int roomNumber) {
        log.info("Check-out request for roomNumber={}", roomNumber);

        LocalDate today = LocalDate.now();

        try {
            Booking booking = bookingDAO
                    .findActiveByRoomNumber(roomNumber, today)
                    .orElseThrow(() ->
                            new BookingNotFoundException("No active booking for room " + roomNumber)
                    );

            booking.forceCheckOut(today);
            bookingDAO.update(booking);

            log.info("Check-out successful for roomNumber={}", roomNumber);
        } catch (Exception e) {
            log.error("Failed to check out roomNumber={}", roomNumber, e);
            throw new BookingException("Failed to check out", e);
        }
    }

    public BigDecimal calculateGuestRoomCost(String guestName) {
        Guest guest = guestManager.getGuestByName(guestName);
        Long guestId = guest.getId();
        return bookingDAO.findByGuestId(guestId).stream()
                .map(Booking::calculateTotalRoomCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateGuestServicesCost(String guestName) {
        Guest guest = guestManager.getGuestByName(guestName);
        Long guestId = guest.getId();
        return bookingDAO.findByGuestId(guestId).stream()
                .map(Booking::calculateTotalServicesCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateGuestTotalCost(String guestName) {
        log.info("Calculating total cost for guest='{}'", guestName);

        BigDecimal total = calculateGuestRoomCost(guestName)
                .add(calculateGuestServicesCost(guestName));

        log.info("Total cost calculated for guest='{}': {}", guestName, total);
        return total;
    }

    public void exportBookingToCSV(String path) {
        log.info("Exporting bookings to CSV, path='{}'", path);

        if (path == null || path.isBlank()) {
            throw new BookingCsvException("CSV export path cannot be empty.");
        }

        File file = new File(path);

        if (file.isDirectory()) {
            file = new File(file, "bookings.csv");
        } else if (!file.getName().toLowerCase().endsWith(".csv")) {
            throw new BookingCsvException(
                    "Invalid file format. CSV file expected: " + file.getName()
            );
        }

        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println("id;guestId;roomId;checkIn;checkOut;serviceIds");

            bookingDAO.findAllWithRelations().forEach(b -> {

                String servicesCsv = b.getServices().stream()
                        .map(bs -> bs.getService().getId())
                        .map(String::valueOf)
                        .collect(Collectors.joining(","));

                writer.println(
                        b.getId() + ";" +
                                b.getGuest().getId() + ";" +
                                b.getRoom().getId() + ";" +
                                b.getCheckInDate() + ";" +
                                b.getCheckOutDate() + ";" +
                                servicesCsv
                );
            });
            log.info("Bookings successfully exported to CSV: {}", file.getAbsolutePath());
        } catch (IOException | DAOException e) {
            log.error("Failed to export bookings to CSV", e);
            throw new BookingCsvException("Failed to export bookings: " + e.getMessage());
        }
    }

    public void importBookingFromCSV(String path) {
        log.info("Importing bookings from CSV, path='{}'", path);

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine();
            List<Booking> imported = new ArrayList<>();

            String line;
            while ((line = br.readLine()) != null) {

                String[] p = line.split(";", -1);

                Long guestId = Long.parseLong(p[1]);
                Long roomId = Long.parseLong(p[2]);

                Guest guest = guestManager.findById(guestId)
                        .orElseThrow(() ->
                                new BookingCsvException("Guest not found: " + guestId));

                Room room = roomManager.findById(roomId)
                        .orElseThrow(() ->
                                new BookingCsvException("Room not found: " + roomId));

                Booking booking = new Booking(
                        guest,
                        room,
                        LocalDate.parse(p[3]),
                        LocalDate.parse(p[4])
                );
                if (!p[5].isEmpty()) {
                    for (String s : p[5].split(",")) {
                        String[] parts = s.split(":");
                        Long serviceId = Long.parseLong(parts[0]);
                        LocalDate serviceDate = LocalDate.parse(parts[1]);
                        serviceManager.findById(serviceId)
                                .ifPresent(service -> booking.addService(service, serviceDate));
                    }
                }

                imported.add(booking);
            }
            addBookingsBatch(imported);
            log.info("Bookings successfully imported from CSV, count={}", imported.size());
        } catch (Exception e) {
            log.error("Failed to import bookings from CSV", e);
            throw new BookingCsvException("Failed to import bookings: " + e.getMessage());
        }
    }
}
