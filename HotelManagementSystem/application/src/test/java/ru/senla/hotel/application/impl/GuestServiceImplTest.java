package ru.senla.hotel.application.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.senla.hotel.application.exception.guest.GuestCsvException;
import ru.senla.hotel.application.exception.guest.GuestException;
import ru.senla.hotel.application.exception.guest.GuestNotFoundException;
import ru.senla.hotel.domain.entity.Guest;
import ru.senla.hotel.infrastructure.repository.GuestRepository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GuestServiceImplTest {

    @Mock
    private GuestRepository guestRepository;
    @InjectMocks
    private GuestServiceImpl service;

    @TempDir
    Path tempDir;

    @Test
    void findByIdShouldReturnGuestWhenExists() {
        Guest guest = new Guest(1L, "Ann");
        when(guestRepository.findById(1L)).thenReturn(guest);

        Optional<Guest> result = service.findById(1L);

        assertEquals("Ann", result.orElseThrow().getName());
    }

    @Test
    void findByIdShouldWrapExceptions() {
        when(guestRepository.findById(1L)).thenThrow(new RuntimeException("db"));

        assertThrows(GuestException.class, () -> service.findById(1L));
    }

    @Test
    void findGuestByNameShouldReturnGuestWhenExists() {
        when(guestRepository.findByName("Ann")).thenReturn(Optional.of(new Guest(1L, "Ann")));

        assertEquals("Ann", service.findGuestByName("Ann").orElseThrow().getName());
    }

    @Test
    void findGuestByNameShouldWrapExceptions() {
        when(guestRepository.findByName("Ann")).thenThrow(new RuntimeException("db"));

        assertThrows(GuestException.class, () -> service.findGuestByName("Ann"));
    }

    @Test
    void getAllGuestsShouldReturnList() {
        when(guestRepository.findAll()).thenReturn(List.of(new Guest(1L, "Ann")));

        assertEquals(1, service.getAllGuests().size());
    }

    @Test
    void getAllGuestsShouldWrapExceptions() {
        when(guestRepository.findAll()).thenThrow(new RuntimeException("db"));

        assertThrows(GuestException.class, service::getAllGuests);
    }

    @Test
    void getGuestByNameShouldReturnGuest() {
        when(guestRepository.findByName("Ann")).thenReturn(Optional.of(new Guest(1L, "Ann")));

        assertEquals("Ann", service.getGuestByName("Ann").getName());
    }

    @Test
    void getGuestByNameShouldThrowWhenNotFound() {
        when(guestRepository.findByName("Ann")).thenReturn(Optional.empty());

        assertThrows(GuestNotFoundException.class, () -> service.getGuestByName("Ann"));
    }

    @Test
    void addGuestShouldSaveWhenUnique() {
        Guest guest = new Guest(1L, "Ann");
        when(guestRepository.findByName("Ann")).thenReturn(Optional.empty());
        when(guestRepository.save(guest)).thenReturn(guest);

        assertEquals("Ann", service.addGuest(guest).getName());
    }

    @Test
    void addGuestShouldWrapErrors() {
        Guest guest = new Guest(1L, "Ann");
        when(guestRepository.findByName("Ann")).thenThrow(new RuntimeException("db"));

        assertThrows(GuestException.class, () -> service.addGuest(guest));
    }

    @Test
    void addGuestsBatchShouldSaveAll() {
        Guest g1 = new Guest(1L, "Ann");
        when(guestRepository.findByName("Ann")).thenReturn(Optional.empty());

        service.addGuestsBatch(List.of(g1));

        verify(guestRepository).save(g1);
    }

    @Test
    void addGuestsBatchShouldWrapErrors() {
        Guest g1 = new Guest(1L, "Ann");
        when(guestRepository.findByName("Ann")).thenThrow(new RuntimeException("db"));

        assertThrows(GuestException.class, () -> service.addGuestsBatch(List.of(g1)));
    }

    @Test
    void exportGuestToCsvShouldCreateFile() {
        Path file = tempDir.resolve("guests.csv");
        when(guestRepository.findAll()).thenReturn(List.of(new Guest(1L, "Ann")));

        service.exportGuestToCSV(file.toString());

        org.junit.jupiter.api.Assertions.assertTrue(Files.exists(file));
    }

    @Test
    void exportGuestToCsvShouldFailForInvalidPath() {
        assertThrows(GuestCsvException.class, () -> service.exportGuestToCSV(""));
    }

    @Test
    void importGuestFromCsvShouldLoadAndPersist() throws Exception {
        Path file = tempDir.resolve("in.csv");
        Files.writeString(file, "id;name\n1;Ann\n");
        when(guestRepository.findByName("Ann")).thenReturn(Optional.empty());

        service.importGuestFromCSV(file.toString());

        verify(guestRepository).save(any(Guest.class));
    }

    @Test
    void importGuestFromCsvShouldWrapErrors() {
        assertThrows(GuestCsvException.class, () -> service.importGuestFromCSV(tempDir.resolve("none.csv").toString()));
    }
}
