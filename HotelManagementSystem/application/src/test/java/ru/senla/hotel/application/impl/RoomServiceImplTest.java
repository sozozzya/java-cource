package ru.senla.hotel.application.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.senla.hotel.application.exception.room.FeatureDisabledException;
import ru.senla.hotel.application.exception.room.InvalidRoomPriceException;
import ru.senla.hotel.application.exception.room.RoomCsvException;
import ru.senla.hotel.application.exception.room.RoomException;
import ru.senla.hotel.application.exception.room.RoomNotFoundException;
import ru.senla.hotel.application.util.sorting.ComparatorRegistry;
import ru.senla.hotel.application.util.sorting.SortingService;
import ru.senla.hotel.domain.entity.Room;
import ru.senla.hotel.domain.enums.SortField;
import ru.senla.hotel.infrastructure.config.ApplicationConfig;
import ru.senla.hotel.infrastructure.repository.BookingRepository;
import ru.senla.hotel.infrastructure.repository.RoomRepository;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoomServiceImplTest {

    @Mock
    private RoomRepository roomRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ApplicationConfig config;
    @Mock
    private SortingService sortingService;
    @Mock
    private ComparatorRegistry comparatorRegistry;
    @InjectMocks
    private RoomServiceImpl service;

    @TempDir
    Path tempDir;

    @Test
    void findByIdShouldReturnRoom() {
        Room room = new Room(1L, 101, 2, 3, BigDecimal.TEN);
        when(roomRepository.findById(1L)).thenReturn(room);

        assertEquals(101, service.findById(1L).orElseThrow().getNumber());
    }

    @Test
    void findByIdShouldWrapRepositoryException() {
        when(roomRepository.findById(1L)).thenThrow(new ru.senla.hotel.infrastructure.exception.RepositoryException("x"));

        assertThrows(RoomException.class, () -> service.findById(1L));
    }

    @Test
    void getRoomByNumberShouldReturnRoom() {
        Room room = new Room(1L, 101, 2, 3, BigDecimal.TEN);
        when(roomRepository.findByNumber(101)).thenReturn(Optional.of(room));

        assertEquals(101, service.getRoomByNumber(101).getNumber());
    }

    @Test
    void getRoomByNumberShouldThrowWhenNotFound() {
        when(roomRepository.findByNumber(101)).thenReturn(Optional.empty());

        assertThrows(RoomNotFoundException.class, () -> service.getRoomByNumber(101));
    }

    @Test
    void getAllRoomsShouldReturnSortedList() {
        List<Room> rooms = List.of(new Room(1L, 101, 2, 3, BigDecimal.TEN));
        Comparator<Room> cmp = (a, b) -> 0;
        when(roomRepository.findAll()).thenReturn(rooms);
        when(comparatorRegistry.roomComparator(SortField.PRICE)).thenReturn(cmp);
        when(sortingService.sort(rooms, cmp)).thenReturn(rooms);

        assertEquals(1, service.getAllRooms(SortField.PRICE).size());
    }

    @Test
    void getAllRoomsShouldThrowWhenSortFails() {
        when(roomRepository.findAll()).thenThrow(new RuntimeException("db"));

        assertThrows(RuntimeException.class, () -> service.getAllRooms(null));
    }

    @Test
    void addRoomShouldPersistUniqueRoom() {
        Room room = new Room(1L, 101, 2, 3, BigDecimal.TEN);
        when(roomRepository.findByNumber(101)).thenReturn(Optional.empty());
        when(roomRepository.save(room)).thenReturn(room);

        assertEquals(101, service.addRoom(room).getNumber());
    }

    @Test
    void addRoomShouldWrapErrors() {
        Room room = new Room(1L, 101, 2, 3, BigDecimal.TEN);
        when(roomRepository.findByNumber(101)).thenThrow(new RuntimeException("db"));

        assertThrows(RoomException.class, () -> service.addRoom(room));
    }

    @Test
    void addRoomsBatchShouldSaveAll() {
        Room room = new Room(1L, 101, 2, 3, BigDecimal.TEN);
        when(roomRepository.findByNumber(101)).thenReturn(Optional.empty());

        service.addRoomsBatch(List.of(room));

        verify(roomRepository).save(room);
    }

    @Test
    void addRoomsBatchShouldWrapErrors() {
        when(roomRepository.findByNumber(101)).thenThrow(new RuntimeException("db"));

        assertThrows(RoomException.class,
                () -> service.addRoomsBatch(List.of(new Room(1L, 101, 2, 3, BigDecimal.TEN))));
    }

    @Test
    void changeRoomPriceShouldUpdatePrice() {
        Room room = new Room(1L, 101, 2, 3, BigDecimal.TEN);
        when(roomRepository.findByNumber(101)).thenReturn(Optional.of(room));

        service.changeRoomPrice(101, BigDecimal.valueOf(20));

        verify(roomRepository).update(room);
    }

    @Test
    void changeRoomPriceShouldThrowForNegative() {
        assertThrows(InvalidRoomPriceException.class,
                () -> service.changeRoomPrice(101, BigDecimal.valueOf(-1)));
    }

    @Test
    void isRoomStatusChangeEnabledShouldReadConfig() {
        when(config.isRoomStatusChangeEnabled()).thenReturn(true);

        org.junit.jupiter.api.Assertions.assertTrue(service.isRoomStatusChangeEnabled());
    }

    @Test
    void isRoomStatusChangeEnabledShouldReturnFalseWhenDisabled() {
        when(config.isRoomStatusChangeEnabled()).thenReturn(false);

        org.junit.jupiter.api.Assertions.assertFalse(service.isRoomStatusChangeEnabled());
    }

    @Test
    void changeMaintenanceStatusShouldUpdateStatusWhenAllowed() {
        Room room = new Room(1L, 101, 2, 3, BigDecimal.TEN);
        when(config.isRoomStatusChangeEnabled()).thenReturn(true);
        when(roomRepository.findByNumber(101)).thenReturn(Optional.of(room));
        when(bookingRepository.existsFutureBookings(1L, java.time.LocalDate.now())).thenReturn(false);

        service.changeMaintenanceStatus(101, true);

        verify(roomRepository).update(room);
    }

    @Test
    void changeMaintenanceStatusShouldThrowWhenFeatureDisabled() {
        when(config.isRoomStatusChangeEnabled()).thenReturn(false);

        assertThrows(FeatureDisabledException.class, () -> service.changeMaintenanceStatus(101, true));
    }

    @Test
    void exportRoomToCsvShouldCreateFile() {
        Path file = tempDir.resolve("rooms.csv");
        when(roomRepository.findAll()).thenReturn(List.of(new Room(1L, 101, 2, 3, BigDecimal.TEN)));

        service.exportRoomToCSV(file.toString());

        org.junit.jupiter.api.Assertions.assertTrue(Files.exists(file));
    }

    @Test
    void exportRoomToCsvShouldFailForBlankPath() {
        assertThrows(RoomCsvException.class, () -> service.exportRoomToCSV(""));
    }

    @Test
    void importRoomFromCsvShouldReadAndSave() throws Exception {
        Path file = tempDir.resolve("rooms-import.csv");
        Files.writeString(file, "id;number;capacity;stars;pricePerNight;isUnderMaintenance\n1;101;2;3;10.00;false\n");
        when(roomRepository.findByNumber(101)).thenReturn(Optional.empty());

        service.importRoomFromCSV(file.toString());

        verify(roomRepository).save(any(Room.class));
    }

    @Test
    void importRoomFromCsvShouldWrapErrors() {
        assertThrows(RoomCsvException.class,
                () -> service.importRoomFromCSV(tempDir.resolve("missing.csv").toString()));
    }
}
