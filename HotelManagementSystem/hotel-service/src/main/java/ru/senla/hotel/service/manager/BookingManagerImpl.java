package ru.senla.hotel.service.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.senla.hotel.config.ApplicationConfig;
import ru.senla.hotel.repository.BookingRepository;
import ru.senla.hotel.repository.exception.RepositoryException;
import ru.senla.hotel.domain.model.Booking;
import ru.senla.hotel.domain.model.Guest;
import ru.senla.hotel.domain.model.Room;
import ru.senla.hotel.domain.model.StayRecord;
import ru.senla.hotel.service.BookingManager;
import ru.senla.hotel.service.GuestManager;
import ru.senla.hotel.service.RoomManager;
import ru.senla.hotel.service.ServiceManager;
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

@Service
@Transactional
public class BookingManagerImpl implements BookingManager {

    private static final Logger log = LoggerFactory.getLogger(BookingManagerImpl.class);

    private final BookingRepository bookingRepository;
    private final RoomManager roomManager;
    private final GuestManager guestManager;
    private final ServiceManager serviceManager;
    private final ApplicationConfig config;

    public BookingManagerImpl(BookingRepository bookingRepository,
                              RoomManager roomManager,
                              GuestManager guestManager,
                              ServiceManager serviceManager,
                              ApplicationConfig config) {
        this.bookingRepository = bookingRepository;
        this.roomManager = roomManager;
        this.guestManager = guestManager;
        this.serviceManager = serviceManager;
        this.config = config;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Booking> getActiveBookings() {
        LocalDate today = LocalDate.now();
        log.debug("Fetching active bookings for date={}", today);

        try {
            return bookingRepository.findActiveByDate(today);
        } catch (Exception e) {
            log.error("Failed to fetch active bookings", e);
            throw new BookingException("Failed to fetch active bookings", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Guest> getCurrentGuests() {
        log.info("Fetching current guests");
        return getActiveBookings().stream()
                .map(Booking::getGuest)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }

    @Override
    public long getCurrentGuestsCount() {
        return getCurrentGuests().size();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isRoomFree(Room room, LocalDate checkIn, LocalDate checkOut) {
        log.info("Checking room availability, roomNumber={}, period {} - {}",
                room.getNumber(), checkIn, checkOut);
        try {
            return bookingRepository
                    .findBookingsByRoomAndPeriod(room.getId(), checkIn, checkOut)
                    .isEmpty();
        } catch (Exception e) {
            log.error("Failed to check room availability", e);
            throw new BookingException("Failed to check room availability", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Room> getAvailableRooms() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        log.info("Fetching available rooms for today");

        return roomManager.getAllRooms().stream()
                .filter(r -> !r.isUnderMaintenance())
                .filter(r -> isRoomFree(r, today, tomorrow))
                .toList();
    }

    @Override
    public long countAvailableRooms() {
        return getAvailableRooms().size();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Room> getRoomsFreeByDate(LocalDate date) {
        return roomManager.getAllRooms().stream()
                .filter(room ->
                        bookingRepository.findAll().stream()
                                .noneMatch(b ->
                                        b.getRoom().equals(room)
                                                && !b.getCheckOutDate().isBefore(date)
                                )
                )
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<StayRecord> getRoomHistory(int roomNumber) {
        Room room = roomManager.getRoomByNumber(roomNumber);

        try {
            return bookingRepository
                    .findCompletedByRoomId(room.getId())
                    .stream()
                    .limit(config.getRoomHistorySize())
                    .map(b -> new StayRecord(
                            b.getGuest().getName(),
                            b.getCheckInDate(),
                            b.getCheckOutDate()
                    ))
                    .toList();
        } catch (Exception e) {
            log.error("Failed to fetch room history", e);
            throw new BookingException("Failed to fetch room history", e);
        }
    }

    @Override
    public Booking addBooking(Booking booking) {
        log.info(
                "Adding booking: guestId={}, roomId={}, checkIn={}, checkOut={}",
                booking.getGuest() != null ? booking.getGuest().getId() : null,
                booking.getRoom() != null ? booking.getRoom().getId() : null,
                booking.getCheckInDate(),
                booking.getCheckOutDate()
        );

        validateBooking(booking);

        try {
            ensureNoBookingConflict(booking);
            Booking saved = bookingRepository.save(booking);

            log.info("Booking successfully added, bookingId={}", saved.getId());
            return saved;
        } catch (Exception e) {
            log.error("Failed to add booking", e);
            throw new BookingException("Failed to add booking", e);
        }
    }

    @Override
    public void addBookingsBatch(List<Booking> bookings) {
        log.info("Importing bookings batch, size={}", bookings.size());

        try {
            for (Booking booking : bookings) {
                validateBooking(booking);

                ensureNoBookingConflict(booking);
                bookingRepository.save(booking);
            }
            log.info("Bookings batch successfully imported, count={}", bookings.size());
        } catch (Exception e) {
            log.error("Failed to import bookings batch", e);
            throw new BookingException("Failed to import bookings batch", e);
        }
    }

    @Override
    public void checkIn(String guestName,
                        int roomNumber,
                        LocalDate checkIn,
                        LocalDate checkOut) {
        log.info("Check-in request: guest='{}', roomNumber={}, checkIn={}, checkOut={}",
                guestName, roomNumber, checkIn, checkOut
        );

        validateDates(checkIn, checkOut);

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

    @Override
    public void checkOut(int roomNumber) {
        log.info("Check-out request for roomNumber={}", roomNumber);

        LocalDate today = LocalDate.now();

        try {
            Booking booking = bookingRepository
                    .findActiveByRoomNumber(roomNumber, today)
                    .orElseThrow(() ->
                            new BookingNotFoundException("No active booking for room " + roomNumber)
                    );

            booking.forceCheckOut(today);
            bookingRepository.update(booking);

            log.info("Check-out successful for roomNumber={}", roomNumber);
        } catch (Exception e) {
            log.error("Failed to check out roomNumber={}", roomNumber, e);
            throw new BookingException("Failed to check out", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateGuestRoomCost(String guestName) {
        Guest guest = guestManager.getGuestByName(guestName);
        Long guestId = guest.getId();
        return bookingRepository.findByGuestId(guestId).stream()
                .map(Booking::calculateTotalRoomCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateGuestServicesCost(String guestName) {
        Guest guest = guestManager.getGuestByName(guestName);
        Long guestId = guest.getId();
        return bookingRepository.findByGuestId(guestId).stream()
                .map(Booking::calculateTotalServicesCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateGuestTotalCost(String guestName) {
        log.info("Calculating total cost for guest='{}'", guestName);

        BigDecimal total = calculateGuestRoomCost(guestName)
                .add(calculateGuestServicesCost(guestName));

        log.info("Total cost calculated for guest='{}': {}", guestName, total);
        return total;
    }

    @Override
    public void exportBookingToCSV(String path) {
        log.info("Exporting bookings to CSV, path='{}'", path);

        if (path == null || path.isBlank()) {
            throw new BookingCsvException("CSV export path cannot be empty.");
        }

        File file = new File(path);

        if (file.isDirectory()) {
            file = new File(file, "bookings.csv");
        } else if (!file.getName().toLowerCase().endsWith(".csv")) {
            throw new BookingCsvException("Invalid file format. CSV file expected: " + file.getName());
        }

        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println("id;guestId;roomId;checkIn;checkOut;serviceIds");

            bookingRepository.findAllWithRelations().forEach(b -> {

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
        } catch (IOException | RepositoryException e) {
            log.error("Failed to export bookings to CSV", e);
            throw new BookingCsvException("Failed to export bookings: " + e.getMessage());
        }
    }

    @Override
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

    private void ensureNoBookingConflict(Booking booking) {
        boolean conflict = !bookingRepository
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

    private void validateBooking(Booking booking) {
        if (booking.getGuest() == null || booking.getRoom() == null) {
            throw new BookingException("Booking must have guest and room.");
        }
    }

    private void validateDates(LocalDate checkIn, LocalDate checkOut) {
        if (checkOut.isBefore(checkIn)) {
            throw new InvalidBookingDatesException(checkIn, checkOut);
        }
        if (checkIn.isBefore(LocalDate.now())) {
            throw new InvalidBookingDatesException(checkIn, LocalDate.now());
        }
    }
}
