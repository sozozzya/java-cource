package ru.senla.hotel.presentation.api.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.senla.hotel.application.service.BookingService;
import ru.senla.hotel.domain.entity.Booking;
import ru.senla.hotel.domain.entity.StayRecord;
import ru.senla.hotel.domain.entity.Room;
import ru.senla.hotel.domain.enums.SortField;
import ru.senla.hotel.presentation.dto.request.CsvPathRequest;
import ru.senla.hotel.presentation.dto.request.booking.CheckInRequest;
import ru.senla.hotel.presentation.dto.response.booking.AvailableRoomResponse;
import ru.senla.hotel.presentation.dto.response.booking.BookingResponse;
import ru.senla.hotel.presentation.dto.response.booking.CountResponse;
import ru.senla.hotel.presentation.dto.response.booking.RoomHistoryResponse;
import ru.senla.hotel.presentation.mapper.BookingMapper;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    @Mock
    private BookingService bookingService;
    @Mock
    private BookingMapper mapper;
    @InjectMocks
    private BookingController controller;

    @Test
    void getActiveBookingsShouldReturnMappedBookingsWhenServiceResponds() {
        Booking booking = mock(Booking.class);
        BookingResponse bookingResponse = mock(BookingResponse.class);
        when(bookingService.getActiveBookings(SortField.NAME)).thenReturn(List.of(booking));
        when(mapper.toResponse(booking)).thenReturn(bookingResponse);

        ResponseEntity<List<BookingResponse>> response = controller.getActiveBookings(SortField.NAME);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getActiveBookingsShouldPropagateExceptionWhenServiceFails() {
        when(bookingService.getActiveBookings(SortField.NAME)).thenThrow(new RuntimeException("boom"));

        assertThrows(RuntimeException.class, () -> controller.getActiveBookings(SortField.NAME));
    }

    @Test
    void getAvailableRoomsShouldReturnMappedRoomsWhenServiceResponds() {
        Room room = mock(Room.class);
        AvailableRoomResponse roomResponse = mock(AvailableRoomResponse.class);
        when(bookingService.getAvailableRooms(SortField.CAPACITY)).thenReturn(List.of(room));
        when(mapper.toRoomResponse(room)).thenReturn(roomResponse);

        ResponseEntity<List<AvailableRoomResponse>> response = controller.getAvailableRooms(SortField.CAPACITY);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getAvailableRoomsShouldPropagateExceptionWhenServiceFails() {
        when(bookingService.getAvailableRooms(null)).thenThrow(new RuntimeException("boom"));

        assertThrows(RuntimeException.class, () -> controller.getAvailableRooms(null));
    }

    @Test
    void countAvailableRoomsShouldReturnCounterWhenServiceResponds() {
        when(bookingService.countAvailableRooms()).thenReturn(7L);

        ResponseEntity<CountResponse> response = controller.countAvailableRooms();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(7L, response.getBody().getValue());
    }

    @Test
    void countAvailableRoomsShouldPropagateExceptionWhenServiceFails() {
        when(bookingService.countAvailableRooms()).thenThrow(new RuntimeException("boom"));

        assertThrows(RuntimeException.class, controller::countAvailableRooms);
    }

    @Test
    void getRoomsFreeByDateShouldReturnMappedRoomsWhenDateProvided() {
        LocalDate date = LocalDate.of(2026, 4, 10);
        Room room = mock(Room.class);
        AvailableRoomResponse roomResponse = mock(AvailableRoomResponse.class);
        when(bookingService.getRoomsFreeByDate(date)).thenReturn(List.of(room));
        when(mapper.toRoomResponse(room)).thenReturn(roomResponse);

        ResponseEntity<List<AvailableRoomResponse>> response = controller.getRoomsFreeByDate(date);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getRoomsFreeByDateShouldPropagateExceptionWhenServiceFails() {
        LocalDate date = LocalDate.of(2026, 4, 11);
        when(bookingService.getRoomsFreeByDate(date)).thenThrow(new RuntimeException("boom"));

        assertThrows(RuntimeException.class, () -> controller.getRoomsFreeByDate(date));
    }

    @Test
    void getRoomHistoryShouldReturnMappedHistoryWhenServiceResponds() {
        StayRecord record = mock(StayRecord.class);
        RoomHistoryResponse historyResponse = mock(RoomHistoryResponse.class);
        when(bookingService.getRoomHistory(101)).thenReturn(List.of(record));
        when(mapper.toHistoryResponse(record)).thenReturn(historyResponse);

        ResponseEntity<List<RoomHistoryResponse>> response = controller.getRoomHistory(101);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getRoomHistoryShouldPropagateExceptionWhenServiceFails() {
        when(bookingService.getRoomHistory(404)).thenThrow(new RuntimeException("boom"));

        assertThrows(RuntimeException.class, () -> controller.getRoomHistory(404));
    }

    @Test
    void checkInShouldReturnCreatedWhenBookingIsCreated() {
        CheckInRequest request = mock(CheckInRequest.class);
        when(request.getGuestName()).thenReturn("Alex");
        when(request.getRoomNumber()).thenReturn(12);
        when(request.getCheckIn()).thenReturn(LocalDate.of(2026, 4, 10));
        when(request.getCheckOut()).thenReturn(LocalDate.of(2026, 4, 12));

        ResponseEntity<Void> response = controller.checkIn(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(bookingService).checkIn("Alex", 12,
                LocalDate.of(2026, 4, 10), LocalDate.of(2026, 4, 12));
    }

    @Test
    void checkInShouldPropagateExceptionWhenServiceFails() {
        CheckInRequest request = mock(CheckInRequest.class);
        when(request.getGuestName()).thenReturn("Alex");
        when(request.getRoomNumber()).thenReturn(12);
        when(request.getCheckIn()).thenReturn(LocalDate.of(2026, 4, 10));
        when(request.getCheckOut()).thenReturn(LocalDate.of(2026, 4, 12));
        doThrow(new RuntimeException("conflict")).when(bookingService)
                .checkIn("Alex", 12, LocalDate.of(2026, 4, 10), LocalDate.of(2026, 4, 12));

        assertThrows(RuntimeException.class, () -> controller.checkIn(request));
    }

    @Test
    void checkOutShouldReturnNoContentWhenCheckoutSucceeds() {
        ResponseEntity<Void> response = controller.checkOut(25);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(bookingService).checkOut(25);
    }

    @Test
    void checkOutShouldPropagateExceptionWhenServiceFails() {
        doThrow(new RuntimeException("not found")).when(bookingService).checkOut(99);

        assertThrows(RuntimeException.class, () -> controller.checkOut(99));
    }

    @Test
    void exportCsvShouldReturnOkWhenExportSucceeds() {
        CsvPathRequest request = mock(CsvPathRequest.class);
        when(request.getPath()).thenReturn("/tmp/export.csv");

        ResponseEntity<Void> response = controller.exportCSV(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(bookingService).exportBookingToCSV("/tmp/export.csv");
    }

    @Test
    void exportCsvShouldPropagateExceptionWhenServiceFails() {
        CsvPathRequest request = mock(CsvPathRequest.class);
        when(request.getPath()).thenReturn("/tmp/export.csv");
        doThrow(new RuntimeException("io")).when(bookingService).exportBookingToCSV("/tmp/export.csv");

        assertThrows(RuntimeException.class, () -> controller.exportCSV(request));
    }

    @Test
    void importCsvShouldReturnOkWhenImportSucceeds() {
        CsvPathRequest request = mock(CsvPathRequest.class);
        when(request.getPath()).thenReturn("/tmp/import.csv");

        ResponseEntity<Void> response = controller.importCSV(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(bookingService).importBookingFromCSV("/tmp/import.csv");
    }

    @Test
    void importCsvShouldPropagateExceptionWhenServiceFails() {
        CsvPathRequest request = mock(CsvPathRequest.class);
        when(request.getPath()).thenReturn("/tmp/import.csv");
        doThrow(new RuntimeException("io")).when(bookingService).importBookingFromCSV("/tmp/import.csv");

        assertThrows(RuntimeException.class, () -> controller.importCSV(request));
    }
}
