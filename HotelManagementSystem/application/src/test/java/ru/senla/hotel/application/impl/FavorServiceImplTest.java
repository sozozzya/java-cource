package ru.senla.hotel.application.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.senla.hotel.application.exception.favor.FavorCsvException;
import ru.senla.hotel.application.exception.favor.FavorException;
import ru.senla.hotel.application.exception.favor.FavorNotFoundException;
import ru.senla.hotel.application.exception.favor.InvalidFavorPriceException;
import ru.senla.hotel.application.util.sorting.ComparatorRegistry;
import ru.senla.hotel.application.util.sorting.SortingService;
import ru.senla.hotel.domain.entity.Favor;
import ru.senla.hotel.domain.enums.SortField;
import ru.senla.hotel.infrastructure.repository.FavorRepository;

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
class FavorServiceImplTest {

    @Mock
    private FavorRepository favorRepository;
    @Mock
    private SortingService sortingService;
    @Mock
    private ComparatorRegistry comparatorRegistry;
    @InjectMocks
    private FavorServiceImpl service;

    @TempDir
    Path tempDir;

    @Test
    void findByIdShouldReturnFavor() {
        Favor favor = new Favor(1L, "Spa", BigDecimal.TEN);
        when(favorRepository.findById(1L)).thenReturn(favor);

        assertEquals("Spa", service.findById(1L).orElseThrow().getName());
    }

    @Test
    void findByIdShouldWrapExceptions() {
        when(favorRepository.findById(1L)).thenThrow(new RuntimeException("db"));

        assertThrows(FavorException.class, () -> service.findById(1L));
    }

    @Test
    void findFavorByNameShouldReturnFavor() {
        when(favorRepository.findByName("Spa")).thenReturn(Optional.of(new Favor("Spa", BigDecimal.TEN)));

        assertEquals("Spa", service.findFavorByName("Spa").orElseThrow().getName());
    }

    @Test
    void findFavorByNameShouldWrapErrors() {
        when(favorRepository.findByName("Spa")).thenThrow(new RuntimeException("db"));

        assertThrows(FavorException.class, () -> service.findFavorByName("Spa"));
    }

    @Test
    void getFavorByNameShouldReturnFavor() {
        when(favorRepository.findByName("Spa")).thenReturn(Optional.of(new Favor("Spa", BigDecimal.TEN)));

        assertEquals("Spa", service.getFavorByName("Spa").getName());
    }

    @Test
    void getFavorByNameShouldThrowWhenMissing() {
        when(favorRepository.findByName("Spa")).thenReturn(Optional.empty());

        assertThrows(FavorNotFoundException.class, () -> service.getFavorByName("Spa"));
    }

    @Test
    void getAvailableFavorsShouldReturnSortedList() {
        List<Favor> favors = List.of(new Favor("Spa", BigDecimal.TEN));
        Comparator<Favor> cmp = (a, b) -> 0;
        when(favorRepository.findAll()).thenReturn(favors);
        when(comparatorRegistry.favorComparator(SortField.PRICE)).thenReturn(cmp);
        when(sortingService.sort(favors, cmp)).thenReturn(favors);

        assertEquals(1, service.getAvailableFavors(SortField.PRICE).size());
    }

    @Test
    void getAvailableFavorsShouldWrapErrors() {
        when(favorRepository.findAll()).thenThrow(new RuntimeException("db"));

        assertThrows(FavorException.class, () -> service.getAvailableFavors(null));
    }

    @Test
    void addFavorShouldSaveWhenUnique() {
        Favor favor = new Favor("Spa", BigDecimal.TEN);
        when(favorRepository.findByName("Spa")).thenReturn(Optional.empty());
        when(favorRepository.save(favor)).thenReturn(favor);

        assertEquals("Spa", service.addFavor(favor).getName());
    }

    @Test
    void addFavorShouldWrapErrors() {
        Favor favor = new Favor("Spa", BigDecimal.TEN);
        when(favorRepository.findByName("Spa")).thenThrow(new RuntimeException("db"));

        assertThrows(FavorException.class, () -> service.addFavor(favor));
    }

    @Test
    void addFavorsBatchShouldPersistAll() {
        Favor favor = new Favor("Spa", BigDecimal.TEN);
        when(favorRepository.findByName("Spa")).thenReturn(Optional.empty());

        service.addFavorsBatch(List.of(favor));

        verify(favorRepository).save(favor);
    }

    @Test
    void addFavorsBatchShouldWrapErrors() {
        when(favorRepository.findByName("Spa")).thenThrow(new RuntimeException("db"));

        assertThrows(FavorException.class,
                () -> service.addFavorsBatch(List.of(new Favor("Spa", BigDecimal.TEN))));
    }

    @Test
    void changeFavorPriceShouldUpdateWhenValid() {
        Favor favor = new Favor("Spa", BigDecimal.ONE);
        when(favorRepository.findByName("Spa")).thenReturn(Optional.of(favor));

        service.changeFavorPrice("Spa", BigDecimal.TEN);

        verify(favorRepository).update(favor);
    }

    @Test
    void changeFavorPriceShouldThrowForNegative() {
        assertThrows(InvalidFavorPriceException.class,
                () -> service.changeFavorPrice("Spa", BigDecimal.valueOf(-1)));
    }

    @Test
    void exportFavorToCsvShouldCreateFile() {
        Path file = tempDir.resolve("favors.csv");
        when(favorRepository.findAll()).thenReturn(List.of(new Favor(1L, "Spa", BigDecimal.ONE)));

        service.exportFavorToCSV(file.toString());

        org.junit.jupiter.api.Assertions.assertTrue(Files.exists(file));
    }

    @Test
    void exportFavorToCsvShouldThrowForBlankPath() {
        assertThrows(FavorCsvException.class, () -> service.exportFavorToCSV(" "));
    }

    @Test
    void importFavorFromCsvShouldReadAndSave() throws Exception {
        Path file = tempDir.resolve("favors-import.csv");
        Files.writeString(file, "id;name;price\n1;Spa;12.00\n");
        when(favorRepository.findByName("Spa")).thenReturn(Optional.empty());

        service.importFavorFromCSV(file.toString());

        verify(favorRepository).save(any(Favor.class));
    }

    @Test
    void importFavorFromCsvShouldWrapErrors() {
        assertThrows(FavorCsvException.class,
                () -> service.importFavorFromCSV(tempDir.resolve("missing.csv").toString()));
    }
}
