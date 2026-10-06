package ru.senla.hotel.presentation.api.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.senla.hotel.application.service.BookingService;
import ru.senla.hotel.application.service.RoomService;
import ru.senla.hotel.domain.entity.Room;
import ru.senla.hotel.domain.entity.StayRecord;
import ru.senla.hotel.domain.enums.SortField;
import ru.senla.hotel.presentation.dto.request.CsvPathRequest;
import ru.senla.hotel.presentation.dto.request.room.ChangeMaintenanceStatusRequest;
import ru.senla.hotel.presentation.dto.request.room.ChangeRoomPriceRequest;
import ru.senla.hotel.presentation.dto.request.room.CreateRoomRequest;
import ru.senla.hotel.presentation.dto.response.room.RoomDetailsResponse;
import ru.senla.hotel.presentation.dto.response.room.RoomLastGuestsResponse;
import ru.senla.hotel.presentation.dto.response.room.RoomPriceResponse;
import ru.senla.hotel.presentation.dto.response.room.RoomResponse;
import ru.senla.hotel.presentation.mapper.RoomMapper;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoomControllerTest {

    @Mock
    private RoomService roomService;
    @Mock
    private BookingService bookingService;
    @Mock
    private RoomMapper mapper;
    @InjectMocks
    private RoomController controller;

    @Test
    void getAllRoomsShouldReturnMappedRoomsWhenServiceResponds() {
        Room room = mock(Room.class);
        RoomResponse roomResponse = mock(RoomResponse.class);
        when(roomService.getAllRooms(SortField.STARS)).thenReturn(List.of(room));
        when(mapper.toResponse(room)).thenReturn(roomResponse);

        ResponseEntity<List<RoomResponse>> response = controller.getAllRooms(SortField.STARS);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getAllRoomsShouldPropagateExceptionWhenServiceFails() {
        when(roomService.getAllRooms(null)).thenThrow(new RuntimeException("boom"));

        assertThrows(RuntimeException.class, () -> controller.getAllRooms(null));
    }

    @Test
    void getRoomShouldReturnDetailsWhenRoomExists() {
        Room room = mock(Room.class);
        RoomDetailsResponse details = mock(RoomDetailsResponse.class);
        when(roomService.getRoomByNumber(100)).thenReturn(room);
        when(mapper.toDetails(room)).thenReturn(details);

        ResponseEntity<RoomDetailsResponse> response = controller.getRoom(100);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(details, response.getBody());
    }

    @Test
    void getRoomShouldPropagateExceptionWhenServiceFails() {
        when(roomService.getRoomByNumber(999)).thenThrow(new RuntimeException("not found"));

        assertThrows(RuntimeException.class, () -> controller.getRoom(999));
    }

    @Test
    void getRoomPricesShouldReturnMappedPricesWhenServiceResponds() {
        Room room = mock(Room.class);
        RoomPriceResponse price = mock(RoomPriceResponse.class);
        when(roomService.getAllRooms(SortField.PRICE)).thenReturn(List.of(room));
        when(mapper.toPriceResponse(room)).thenReturn(price);

        ResponseEntity<List<RoomPriceResponse>> response = controller.getRoomPrices();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getRoomPricesShouldPropagateExceptionWhenServiceFails() {
        when(roomService.getAllRooms(SortField.PRICE)).thenThrow(new RuntimeException("boom"));

        assertThrows(RuntimeException.class, controller::getRoomPrices);
    }

    @Test
    void getLastGuestsShouldReturnMappedHistoryWhenServiceResponds() {
        StayRecord record = mock(StayRecord.class);
        RoomLastGuestsResponse lastGuestsResponse = mock(RoomLastGuestsResponse.class);
        when(bookingService.getRoomHistory(55)).thenReturn(List.of(record));
        when(mapper.toLastGuestsResponse(record)).thenReturn(lastGuestsResponse);

        ResponseEntity<List<RoomLastGuestsResponse>> response = controller.getLastGuests(55);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getLastGuestsShouldPropagateExceptionWhenServiceFails() {
        when(bookingService.getRoomHistory(77)).thenThrow(new RuntimeException("boom"));

        assertThrows(RuntimeException.class, () -> controller.getLastGuests(77));
    }

    @Test
    void createRoomShouldReturnCreatedWhenRoomCreated() {
        CreateRoomRequest request = mock(CreateRoomRequest.class);
        Room roomToSave = mock(Room.class);
        Room savedRoom = mock(Room.class);
        RoomResponse roomResponse = mock(RoomResponse.class);

        when(mapper.toEntity(request)).thenReturn(roomToSave);
        when(roomService.addRoom(roomToSave)).thenReturn(savedRoom);
        when(mapper.toResponse(savedRoom)).thenReturn(roomResponse);

        ResponseEntity<RoomResponse> response = controller.createRoom(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(roomResponse, response.getBody());
    }

    @Test
    void createRoomShouldPropagateExceptionWhenServiceFails() {
        CreateRoomRequest request = mock(CreateRoomRequest.class);
        Room roomToSave = mock(Room.class);
        when(mapper.toEntity(request)).thenReturn(roomToSave);
        when(roomService.addRoom(roomToSave)).thenThrow(new RuntimeException("exists"));

        assertThrows(RuntimeException.class, () -> controller.createRoom(request));
    }

    @Test
    void changePriceShouldReturnNoContentWhenUpdateSucceeds() {
        ChangeRoomPriceRequest request = mock(ChangeRoomPriceRequest.class);
        when(request.getPrice()).thenReturn(BigDecimal.valueOf(120));

        ResponseEntity<Void> response = controller.changePrice(12, request);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(roomService).changeRoomPrice(12, BigDecimal.valueOf(120));
    }

    @Test
    void changePriceShouldPropagateExceptionWhenServiceFails() {
        ChangeRoomPriceRequest request = mock(ChangeRoomPriceRequest.class);
        when(request.getPrice()).thenReturn(BigDecimal.valueOf(120));
        doThrow(new RuntimeException("invalid"))
                .when(roomService).changeRoomPrice(12, BigDecimal.valueOf(120));

        assertThrows(RuntimeException.class, () -> controller.changePrice(12, request));
    }

    @Test
    void changeMaintenanceShouldReturnNoContentWhenUpdateSucceeds() {
        ChangeMaintenanceStatusRequest request = mock(ChangeMaintenanceStatusRequest.class);
        when(request.isUnderMaintenance()).thenReturn(true);

        ResponseEntity<Void> response = controller.changeMaintenance(8, request);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(roomService).changeMaintenanceStatus(8, true);
    }

    @Test
    void changeMaintenanceShouldPropagateExceptionWhenServiceFails() {
        ChangeMaintenanceStatusRequest request = mock(ChangeMaintenanceStatusRequest.class);
        when(request.isUnderMaintenance()).thenReturn(false);
        doThrow(new RuntimeException("invalid"))
                .when(roomService).changeMaintenanceStatus(8, false);

        assertThrows(RuntimeException.class, () -> controller.changeMaintenance(8, request));
    }

    @Test
    void exportCsvShouldReturnOkWhenExportSucceeds() {
        CsvPathRequest request = mock(CsvPathRequest.class);
        when(request.getPath()).thenReturn("/tmp/rooms.csv");

        ResponseEntity<Void> response = controller.exportCSV(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(roomService).exportRoomToCSV("/tmp/rooms.csv");
    }

    @Test
    void exportCsvShouldPropagateExceptionWhenExportFails() {
        CsvPathRequest request = mock(CsvPathRequest.class);
        when(request.getPath()).thenReturn("/tmp/rooms.csv");
        doThrow(new RuntimeException("io")).when(roomService).exportRoomToCSV("/tmp/rooms.csv");

        assertThrows(RuntimeException.class, () -> controller.exportCSV(request));
    }

    @Test
    void importCsvShouldReturnOkWhenImportSucceeds() {
        CsvPathRequest request = mock(CsvPathRequest.class);
        when(request.getPath()).thenReturn("/tmp/rooms.csv");

        ResponseEntity<Void> response = controller.importCSV(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(roomService).importRoomFromCSV("/tmp/rooms.csv");
    }

    @Test
    void importCsvShouldPropagateExceptionWhenImportFails() {
        CsvPathRequest request = mock(CsvPathRequest.class);
        when(request.getPath()).thenReturn("/tmp/rooms.csv");
        doThrow(new RuntimeException("io")).when(roomService).importRoomFromCSV("/tmp/rooms.csv");

        assertThrows(RuntimeException.class, () -> controller.importCSV(request));
    }
}
