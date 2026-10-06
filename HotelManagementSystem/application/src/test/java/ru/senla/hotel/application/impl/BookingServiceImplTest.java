package ru.senla.hotel.application.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.senla.hotel.application.exception.booking.BookingCsvException;
import ru.senla.hotel.application.exception.booking.BookingException;
import ru.senla.hotel.application.exception.booking.InvalidBookingDatesException;
import ru.senla.hotel.application.exception.booking.RoomUnavailableException;
import ru.senla.hotel.application.service.FavorService;
import ru.senla.hotel.application.service.GuestService;
import ru.senla.hotel.application.service.RoomService;
import ru.senla.hotel.application.util.sorting.ComparatorRegistry;
import ru.senla.hotel.application.util.sorting.SortingService;
import ru.senla.hotel.domain.entity.Booking;
import ru.senla.hotel.domain.entity.Favor;
import ru.senla.hotel.domain.entity.Guest;
import ru.senla.hotel.domain.entity.Room;
import ru.senla.hotel.domain.entity.StayRecord;
import ru.senla.hotel.domain.enums.SortField;
import ru.senla.hotel.infrastructure.config.ApplicationConfig;
import ru.senla.hotel.infrastructure.repository.BookingRepository;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private RoomService roomService;
    @Mock
    private GuestService guestService;
    @Mock
    private FavorService favorService;
    @Mock
    private ApplicationConfig config;
    @Mock
    private SortingService sortingService;
    @Mock
    private ComparatorRegistry comparatorRegistry;
    @InjectMocks
    private BookingServiceImpl service;

    @TempDir
    Path tempDir;

    @Test
    void getActiveBookingsShouldReturnSortedList() {
        List<Booking> list = List.of(sampleBooking());
        Comparator<Booking> cmp = (a, b) -> 0;
        when(bookingRepository.findActiveByDate(any(LocalDate.class))).thenReturn(list);
        when(comparatorRegistry.bookingComparator(SortField.NAME)).thenReturn(cmp);
        when(sortingService.sort(list, cmp)).thenReturn(list);

        assertEquals(1, service.getActiveBookings(SortField.NAME).size());
    }

    @Test
    void getActiveBookingsShouldWrapErrors() {
        when(bookingRepository.findActiveByDate(any(LocalDate.class))).thenThrow(new RuntimeException("db"));

        assertThrows(BookingException.class, () -> service.getActiveBookings(null));
    }

    @Test
    void getCurrentGuestsShouldReturnDistinctGuests() {
        Booking booking = sampleBooking();
        when(bookingRepository.findActiveByDate(any(LocalDate.class))).thenReturn(List.of(booking));
        Comparator<Booking> cmp = (a, b) -> 0;
        when(comparatorRegistry.bookingComparator(null)).thenReturn(cmp);
        when(sortingService.sort(any(), any())).thenReturn(List.of(booking));

        assertEquals(1, service.getCurrentGuests().size());
    }

    @Test
    void getCurrentGuestsShouldPropagateWhenActiveFails() {
        when(bookingRepository.findActiveByDate(any(LocalDate.class))).thenThrow(new RuntimeException("db"));

        assertThrows(BookingException.class, service::getCurrentGuests);
    }

    @Test
    void getCurrentGuestsCountShouldReturnCount() {
        Booking booking = sampleBooking();
        when(bookingRepository.findActiveByDate(any(LocalDate.class))).thenReturn(List.of(booking));
        when(comparatorRegistry.bookingComparator(null)).thenReturn((a, b) -> 0);
        when(sortingService.sort(any(), any())).thenReturn(List.of(booking));

        assertEquals(1, service.getCurrentGuestsCount());
    }

    @Test
    void getCurrentGuestsCountShouldPropagateWhenCurrentFails() {
        when(bookingRepository.findActiveByDate(any(LocalDate.class))).thenThrow(new RuntimeException("db"));

        assertThrows(BookingException.class, service::getCurrentGuestsCount);
    }

    @Test
    void isRoomFreeShouldReturnTrueWhenNoOverlaps() {
        Room room = sampleRoom();
        when(bookingRepository.findBookingsByRoomAndPeriod(any(), any(), any())).thenReturn(List.of());

        assertTrue(service.isRoomFree(room, LocalDate.now(), LocalDate.now().plusDays(1)));
    }

    @Test
    void isRoomFreeShouldWrapErrors() {
        Room room = sampleRoom();
        when(bookingRepository.findBookingsByRoomAndPeriod(any(), any(), any())).thenThrow(new RuntimeException("db"));

        assertThrows(BookingException.class,
                () -> service.isRoomFree(room, LocalDate.now(), LocalDate.now().plusDays(1)));
    }

    @Test
    void getAvailableRoomsShouldReturnFilteredAndSorted() {
        Room room = sampleRoom();
        when(roomService.getAllRooms(null)).thenReturn(List.of(room));
        when(bookingRepository.findBookingsByRoomAndPeriod(any(), any(), any())).thenReturn(List.of());
        when(comparatorRegistry.roomComparator(SortField.PRICE)).thenReturn((a, b) -> 0);
        when(sortingService.sort(any(), any())).thenAnswer(i -> i.getArgument(0));

        assertEquals(1, service.getAvailableRooms(SortField.PRICE).size());
    }

    @Test
    void getAvailableRoomsShouldThrowWhenRoomManagerFails() {
        when(roomService.getAllRooms(null)).thenThrow(new RuntimeException("db"));

        assertThrows(RuntimeException.class, () -> service.getAvailableRooms(null));
    }

    @Test
    void countAvailableRoomsShouldReturnSize() {
        Room room = sampleRoom();
        when(roomService.getAllRooms(null)).thenReturn(List.of(room));
        when(bookingRepository.findBookingsByRoomAndPeriod(any(), any(), any())).thenReturn(List.of());
        when(comparatorRegistry.roomComparator(null)).thenReturn((a, b) -> 0);
        when(sortingService.sort(any(), any())).thenAnswer(i -> i.getArgument(0));

        assertEquals(1, service.countAvailableRooms());
    }

    @Test
    void countAvailableRoomsShouldPropagateWhenAvailableFails() {
        when(roomService.getAllRooms(null)).thenThrow(new RuntimeException("db"));

        assertThrows(RuntimeException.class, service::countAvailableRooms);
    }

    @Test
    void getRoomsFreeByDateShouldReturnFreeRooms() {
        Room room = sampleRoom();
        when(roomService.getAllRooms(null)).thenReturn(List.of(room));
        when(bookingRepository.findAll()).thenReturn(List.of());

        assertEquals(1, service.getRoomsFreeByDate(LocalDate.now()).size());
    }

    @Test
    void getRoomsFreeByDateShouldThrowWhenFindAllFails() {
        when(roomService.getAllRooms(null)).thenReturn(List.of(sampleRoom()));
        when(bookingRepository.findAll()).thenThrow(new RuntimeException("db"));

        assertThrows(RuntimeException.class, () -> service.getRoomsFreeByDate(LocalDate.now()));
    }

    @Test
    void getRoomHistoryShouldReturnRecords() {
        Room room = sampleRoom();
        Booking booking = sampleBooking();
        when(roomService.getRoomByNumber(101)).thenReturn(room);
        when(config.getRoomHistorySize()).thenReturn(5);
        when(bookingRepository.findCompletedByRoomId(1L)).thenReturn(List.of(booking));

        List<StayRecord> result = service.getRoomHistory(101);

        assertEquals(1, result.size());
    }

    @Test
    void getRoomHistoryShouldWrapErrors() {
        when(roomService.getRoomByNumber(101)).thenReturn(sampleRoom());
        when(bookingRepository.findCompletedByRoomId(1L)).thenThrow(new RuntimeException("db"));

        assertThrows(BookingException.class, () -> service.getRoomHistory(101));
    }

    @Test
    void addBookingShouldSaveWhenValidAndNoConflict() {
        Booking booking = sampleBooking();
        when(bookingRepository.findBookingsByRoomAndPeriod(any(), any(), any())).thenReturn(List.of());
        when(bookingRepository.save(booking)).thenReturn(booking);

        assertEquals(booking, service.addBooking(booking));
    }

    @Test
    void addBookingShouldThrowWhenConflictExists() {
        Booking booking = sampleBooking();
        when(bookingRepository.findBookingsByRoomAndPeriod(any(), any(), any())).thenReturn(List.of(sampleBooking()));

        assertThrows(BookingException.class, () -> service.addBooking(booking));
    }

    @Test
    void addBookingsBatchShouldSaveAllWhenValid() {
        Booking booking = sampleBooking();
        when(bookingRepository.findBookingsByRoomAndPeriod(any(), any(), any())).thenReturn(List.of());

        service.addBookingsBatch(List.of(booking));

        verify(bookingRepository).save(booking);
    }

    @Test
    void addBookingsBatchShouldWrapErrors() {
        Booking booking = sampleBooking();
        when(bookingRepository.findBookingsByRoomAndPeriod(any(), any(), any())).thenReturn(List.of(sampleBooking()));

        assertThrows(BookingException.class, () -> service.addBookingsBatch(List.of(booking)));
    }

    @Test
    void checkInShouldCreateBookingWhenAllValid() {
        Guest guest = sampleGuest();
        Room room = sampleRoom();
        when(guestService.getGuestByName("Ann")).thenReturn(guest);
        when(roomService.getRoomByNumber(101)).thenReturn(room);
        when(bookingRepository.findBookingsByRoomAndPeriod(any(), any(), any())).thenReturn(List.of());
        when(bookingRepository.save(any(Booking.class))).thenAnswer(i -> i.getArgument(0));

        service.checkIn("Ann", 101, LocalDate.now(), LocalDate.now().plusDays(1));

        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void checkInShouldThrowForInvalidDates() {
        LocalDate now = LocalDate.now();
        assertThrows(InvalidBookingDatesException.class,
                () -> service.checkIn("Ann", 101, now, now.minusDays(1)));
    }

    @Test
    void checkOutShouldUpdateActiveBooking() {
        Booking booking = sampleBooking();
        when(bookingRepository.findActiveByRoomNumber(any(Integer.class), any(LocalDate.class)))
                .thenReturn(Optional.of(booking));

        service.checkOut(101);

        verify(bookingRepository).update(booking);
    }

    @Test
    void checkOutShouldWrapErrors() {
        when(bookingRepository.findActiveByRoomNumber(any(Integer.class), any(LocalDate.class)))
                .thenReturn(Optional.empty());

        assertThrows(BookingException.class, () -> service.checkOut(101));
    }

    @Test
    void calculateGuestRoomCostShouldReturnSum() {
        Guest guest = sampleGuest();
        Booking booking = sampleBooking();
        when(guestService.getGuestByName("Ann")).thenReturn(guest);
        when(bookingRepository.findByGuestId(1L)).thenReturn(List.of(booking));

        assertTrue(service.calculateGuestRoomCost("Ann").compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void calculateGuestRoomCostShouldThrowWhenGuestMissing() {
        when(guestService.getGuestByName("Ann")).thenThrow(new RuntimeException("missing"));

        assertThrows(RuntimeException.class, () -> service.calculateGuestRoomCost("Ann"));
    }

    @Test
    void calculateGuestServicesCostShouldReturnSum() {
        Guest guest = sampleGuest();
        Booking booking = sampleBooking();
        booking.addService(new Favor(1L, "Spa", BigDecimal.TEN), LocalDate.now());
        when(guestService.getGuestByName("Ann")).thenReturn(guest);
        when(bookingRepository.findByGuestId(1L)).thenReturn(List.of(booking));

        assertEquals(BigDecimal.TEN, service.calculateGuestServicesCost("Ann"));
    }

    @Test
    void calculateGuestServicesCostShouldThrowWhenRepositoryFails() {
        when(guestService.getGuestByName("Ann")).thenReturn(sampleGuest());
        when(bookingRepository.findByGuestId(1L)).thenThrow(new RuntimeException("db"));

        assertThrows(RuntimeException.class, () -> service.calculateGuestServicesCost("Ann"));
    }

    @Test
    void calculateGuestTotalCostShouldReturnCombinedSum() {
        Guest guest = sampleGuest();
        Booking booking = sampleBooking();
        booking.addService(new Favor(1L, "Spa", BigDecimal.TEN), LocalDate.now());
        when(guestService.getGuestByName("Ann")).thenReturn(guest);
        when(bookingRepository.findByGuestId(1L)).thenReturn(List.of(booking));

        assertTrue(service.calculateGuestTotalCost("Ann").compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void calculateGuestTotalCostShouldThrowWhenInnerMethodFails() {
        when(guestService.getGuestByName("Ann")).thenThrow(new RuntimeException("missing"));

        assertThrows(RuntimeException.class, () -> service.calculateGuestTotalCost("Ann"));
    }

    @Test
    void exportBookingToCsvShouldCreateFile() {
        Path file = tempDir.resolve("bookings.csv");
        Booking booking = sampleBooking();
        when(bookingRepository.findAllWithRelations()).thenReturn(List.of(booking));

        service.exportBookingToCSV(file.toString());

        assertTrue(Files.exists(file));
    }

    @Test
    void exportBookingToCsvShouldThrowForBlankPath() {
        assertThrows(BookingCsvException.class, () -> service.exportBookingToCSV(" "));
    }

    @Test
    void importBookingFromCsvShouldReadAndPersist() throws Exception {
        Path file = tempDir.resolve("bookings-import.csv");
        Files.writeString(file, "id;guestId;roomId;checkIn;checkOut;serviceIds\n1;1;1;2026-04-01;2026-04-02;\n");
        when(guestService.findById(1L)).thenReturn(Optional.of(sampleGuest()));
        when(roomService.findById(1L)).thenReturn(Optional.of(sampleRoom()));
        when(bookingRepository.findBookingsByRoomAndPeriod(any(), any(), any())).thenReturn(List.of());

        service.importBookingFromCSV(file.toString());

        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void importBookingFromCsvShouldWrapErrors() {
        assertThrows(BookingCsvException.class,
                () -> service.importBookingFromCSV(tempDir.resolve("missing.csv").toString()));
    }

    private static Guest sampleGuest() {
        return new Guest(1L, "Ann");
    }

    private static Room sampleRoom() {
        return new Room(1L, 101, 2, 3, BigDecimal.valueOf(100));
    }

    private static Booking sampleBooking() {
        return new Booking(sampleGuest(), sampleRoom(), LocalDate.now(), LocalDate.now().plusDays(1));
    }
}
