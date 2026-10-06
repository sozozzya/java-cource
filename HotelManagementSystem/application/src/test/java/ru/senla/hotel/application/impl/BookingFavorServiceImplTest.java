package ru.senla.hotel.application.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.senla.hotel.application.exception.booking.BookingException;
import ru.senla.hotel.application.service.FavorService;
import ru.senla.hotel.application.service.GuestService;
import ru.senla.hotel.application.util.sorting.ComparatorRegistry;
import ru.senla.hotel.application.util.sorting.SortingService;
import ru.senla.hotel.domain.entity.Booking;
import ru.senla.hotel.domain.entity.BookingFavor;
import ru.senla.hotel.domain.entity.Favor;
import ru.senla.hotel.domain.entity.Guest;
import ru.senla.hotel.domain.enums.SortField;
import ru.senla.hotel.infrastructure.repository.BookingFavorRepository;
import ru.senla.hotel.infrastructure.repository.BookingRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingFavorServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private BookingFavorRepository bookingFavorRepository;
    @Mock
    private FavorService favorService;
    @Mock
    private GuestService guestService;
    @Mock
    private SortingService sortingService;
    @Mock
    private ComparatorRegistry comparatorRegistry;
    @InjectMocks
    private BookingFavorServiceImpl service;

    @Test
    void getGuestFavorsShouldReturnSortedList() {
        BookingFavor favor = org.mockito.Mockito.mock(BookingFavor.class);
        List<BookingFavor> list = List.of(favor);
        Comparator<BookingFavor> comparator = (a, b) -> 0;
        when(bookingFavorRepository.findFavorsByGuestName("Ann")).thenReturn(list);
        when(comparatorRegistry.bookingFavorComparator(SortField.DATE)).thenReturn(comparator);
        when(sortingService.sort(list, comparator)).thenReturn(list);

        List<BookingFavor> result = service.getGuestFavors("Ann", SortField.DATE);

        assertEquals(1, result.size());
    }

    @Test
    void getGuestFavorsShouldWrapException() {
        when(bookingFavorRepository.findFavorsByGuestName("Ann")).thenThrow(new RuntimeException("db"));

        assertThrows(BookingException.class, () -> service.getGuestFavors("Ann", null));
    }

    @Test
    void assignFavorToGuestShouldReturnFavorPrice() {
        Guest guest = new Guest(1L, "Ann");
        RoomHelper roomHelper = new RoomHelper(10L, 101);
        Booking booking = new Booking(guest, roomHelper.room, LocalDate.of(2026, 4, 1), LocalDate.of(2026, 4, 20));
        Favor favor = new Favor("spa", BigDecimal.TEN);

        when(guestService.getGuestByName("Ann")).thenReturn(guest);
        when(bookingRepository.findByGuestId(1L)).thenReturn(List.of(booking));
        when(favorService.getFavorByName("spa")).thenReturn(favor);

        BigDecimal result = service.assignFavorToGuest("Ann", "spa", LocalDate.of(2026, 4, 10));

        assertEquals(BigDecimal.TEN, result);
        verify(bookingFavorRepository).save(any(BookingFavor.class));
    }

    @Test
    void assignFavorToGuestShouldWrapWhenNoBooking() {
        Guest guest = new Guest(1L, "Ann");
        when(guestService.getGuestByName("Ann")).thenReturn(guest);
        when(bookingRepository.findByGuestId(1L)).thenReturn(List.of());

        assertThrows(BookingException.class,
                () -> service.assignFavorToGuest("Ann", "spa", LocalDate.of(2026, 4, 10)));
    }

    private static class RoomHelper {
        private final ru.senla.hotel.domain.entity.Room room;

        private RoomHelper(Long id, int number) {
            room = new ru.senla.hotel.domain.entity.Room(id, number, 2, 3, BigDecimal.valueOf(100));
        }
    }
}
