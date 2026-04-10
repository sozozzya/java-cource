package ru.senla.hotel.presentation.api.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.senla.hotel.application.service.BookingFavorService;
import ru.senla.hotel.application.service.BookingService;
import ru.senla.hotel.application.service.GuestService;
import ru.senla.hotel.domain.entity.Booking;
import ru.senla.hotel.domain.entity.BookingFavor;
import ru.senla.hotel.domain.entity.Guest;
import ru.senla.hotel.domain.enums.SortField;
import ru.senla.hotel.presentation.dto.request.CsvPathRequest;
import ru.senla.hotel.presentation.dto.request.guest.CreateGuestRequest;
import ru.senla.hotel.presentation.dto.response.favor.BookingFavorResponse;
import ru.senla.hotel.presentation.dto.response.guest.GuestCountResponse;
import ru.senla.hotel.presentation.dto.response.guest.GuestPaymentResponse;
import ru.senla.hotel.presentation.dto.response.guest.GuestResponse;
import ru.senla.hotel.presentation.dto.response.guest.GuestRoomResponse;
import ru.senla.hotel.presentation.mapper.FavorMapper;
import ru.senla.hotel.presentation.mapper.GuestMapper;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GuestControllerTest {

    @Mock
    private GuestService guestService;
    @Mock
    private BookingService bookingService;
    @Mock
    private GuestMapper guestMapper;
    @Mock
    private BookingFavorService bookingFavorService;
    @Mock
    private FavorMapper favorMapper;
    @InjectMocks
    private GuestController controller;

    @Test
    void getGuestsShouldReturnMappedGuestsWhenServiceResponds() {
        Guest guest = mock(Guest.class);
        GuestResponse guestResponse = mock(GuestResponse.class);
        when(guestService.getAllGuests()).thenReturn(List.of(guest));
        when(guestMapper.toResponse(guest)).thenReturn(guestResponse);

        ResponseEntity<List<GuestResponse>> response = controller.getGuests();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getGuestsShouldPropagateExceptionWhenServiceFails() {
        when(guestService.getAllGuests()).thenThrow(new RuntimeException("boom"));

        assertThrows(RuntimeException.class, controller::getGuests);
    }

    @Test
    void getGuestsWithRoomsShouldReturnMappedBookingsWhenServiceResponds() {
        Booking booking = mock(Booking.class);
        GuestRoomResponse guestRoomResponse = mock(GuestRoomResponse.class);
        when(bookingService.getActiveBookings(SortField.CHECKOUT_DATE)).thenReturn(List.of(booking));
        when(guestMapper.toGuestRoomResponse(booking)).thenReturn(guestRoomResponse);

        ResponseEntity<List<GuestRoomResponse>> response = controller.getGuestsWithRooms(SortField.CHECKOUT_DATE);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getGuestsWithRoomsShouldPropagateExceptionWhenServiceFails() {
        when(bookingService.getActiveBookings(null)).thenThrow(new RuntimeException("boom"));

        assertThrows(RuntimeException.class, () -> controller.getGuestsWithRooms(null));
    }

    @Test
    void getGuestFavorsShouldReturnMappedFavorsWhenServiceResponds() {
        BookingFavor bookingFavor = mock(BookingFavor.class);
        BookingFavorResponse favorResponse = mock(BookingFavorResponse.class);
        when(bookingFavorService.getGuestFavors("Ann", SortField.DATE)).thenReturn(List.of(bookingFavor));
        when(favorMapper.toGuestFavorResponse(bookingFavor)).thenReturn(favorResponse);

        ResponseEntity<List<BookingFavorResponse>> response = controller.getGuestFavors("Ann", SortField.DATE);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getGuestFavorsShouldPropagateExceptionWhenServiceFails() {
        when(bookingFavorService.getGuestFavors("Ann", null)).thenThrow(new RuntimeException("boom"));

        assertThrows(RuntimeException.class, () -> controller.getGuestFavors("Ann", null));
    }

    @Test
    void countGuestsShouldReturnCountWhenServiceResponds() {
        when(bookingService.getCurrentGuestsCount()).thenReturn(5L);

        ResponseEntity<GuestCountResponse> response = controller.countGuests();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5L, response.getBody().getTotalGuests());
    }

    @Test
    void countGuestsShouldPropagateExceptionWhenServiceFails() {
        when(bookingService.getCurrentGuestsCount()).thenThrow(new RuntimeException("boom"));

        assertThrows(RuntimeException.class, controller::countGuests);
    }

    @Test
    void calculatePaymentShouldReturnPaymentResponseWhenServiceResponds() {
        GuestPaymentResponse paymentResponse = mock(GuestPaymentResponse.class);
        when(bookingService.calculateGuestTotalCost("Mike")).thenReturn(BigDecimal.TEN);
        when(guestMapper.toPaymentResponse("Mike", BigDecimal.TEN)).thenReturn(paymentResponse);

        ResponseEntity<GuestPaymentResponse> response = controller.calculatePayment("Mike");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(paymentResponse, response.getBody());
    }

    @Test
    void calculatePaymentShouldPropagateExceptionWhenServiceFails() {
        when(bookingService.calculateGuestTotalCost("Mike")).thenThrow(new RuntimeException("boom"));

        assertThrows(RuntimeException.class, () -> controller.calculatePayment("Mike"));
    }

    @Test
    void createGuestShouldReturnCreatedWhenGuestCreated() {
        CreateGuestRequest request = mock(CreateGuestRequest.class);
        Guest guestToSave = mock(Guest.class);
        Guest savedGuest = mock(Guest.class);
        GuestResponse guestResponse = mock(GuestResponse.class);

        when(guestMapper.toEntity(request)).thenReturn(guestToSave);
        when(guestService.addGuest(guestToSave)).thenReturn(savedGuest);
        when(guestMapper.toResponse(savedGuest)).thenReturn(guestResponse);

        ResponseEntity<GuestResponse> response = controller.createGuest(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(guestResponse, response.getBody());
    }

    @Test
    void createGuestShouldPropagateExceptionWhenServiceFails() {
        CreateGuestRequest request = mock(CreateGuestRequest.class);
        Guest guestToSave = mock(Guest.class);
        when(guestMapper.toEntity(request)).thenReturn(guestToSave);
        when(guestService.addGuest(guestToSave)).thenThrow(new RuntimeException("exists"));

        assertThrows(RuntimeException.class, () -> controller.createGuest(request));
    }

    @Test
    void exportCsvShouldReturnOkWhenExportSucceeds() {
        CsvPathRequest request = mock(CsvPathRequest.class);
        when(request.getPath()).thenReturn("/tmp/guests.csv");

        ResponseEntity<Void> response = controller.exportCSV(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(guestService).exportGuestToCSV("/tmp/guests.csv");
    }

    @Test
    void exportCsvShouldPropagateExceptionWhenExportFails() {
        CsvPathRequest request = mock(CsvPathRequest.class);
        when(request.getPath()).thenReturn("/tmp/guests.csv");
        doThrow(new RuntimeException("io")).when(guestService).exportGuestToCSV("/tmp/guests.csv");

        assertThrows(RuntimeException.class, () -> controller.exportCSV(request));
    }

    @Test
    void importCsvShouldReturnOkWhenImportSucceeds() {
        CsvPathRequest request = mock(CsvPathRequest.class);
        when(request.getPath()).thenReturn("/tmp/guests.csv");

        ResponseEntity<Void> response = controller.importCSV(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(guestService).importGuestFromCSV("/tmp/guests.csv");
    }

    @Test
    void importCsvShouldPropagateExceptionWhenImportFails() {
        CsvPathRequest request = mock(CsvPathRequest.class);
        when(request.getPath()).thenReturn("/tmp/guests.csv");
        doThrow(new RuntimeException("io")).when(guestService).importGuestFromCSV("/tmp/guests.csv");

        assertThrows(RuntimeException.class, () -> controller.importCSV(request));
    }
}
