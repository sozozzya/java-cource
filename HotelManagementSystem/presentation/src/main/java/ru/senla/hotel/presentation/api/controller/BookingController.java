package ru.senla.hotel.presentation.api.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.senla.hotel.application.service.BookingService;
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

@RestController
@RequestMapping(value = "/api/bookings", produces = MediaType.APPLICATION_JSON_VALUE)
public class BookingController {

    private final BookingService bookingService;
    private final BookingMapper mapper;

    public BookingController(BookingService bookingService,
                             BookingMapper mapper) {
        this.bookingService = bookingService;
        this.mapper = mapper;
    }

    @GetMapping("/active")
    public ResponseEntity<List<BookingResponse>> getActiveBookings(
            @RequestParam(name = "sortField", required = false) SortField field) {

        List<BookingResponse> response = bookingService.getActiveBookings(field)
                .stream()
                .map(mapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/available-rooms")
    public ResponseEntity<List<AvailableRoomResponse>> getAvailableRooms(
            @RequestParam(name = "sortField", required = false) SortField field) {

        List<AvailableRoomResponse> rooms = bookingService.getAvailableRooms(field)
                .stream()
                .map(mapper::toRoomResponse)
                .toList();

        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/available-rooms/count")
    public ResponseEntity<CountResponse> countAvailableRooms() {

        CountResponse response = new CountResponse();
        response.setValue(bookingService.countAvailableRooms());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/available-by-date")
    public ResponseEntity<List<AvailableRoomResponse>> getRoomsFreeByDate(
            @RequestParam(name = "date") LocalDate date) {

        List<AvailableRoomResponse> rooms = bookingService.getRoomsFreeByDate(date)
                .stream()
                .map(mapper::toRoomResponse)
                .toList();

        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/rooms/{roomNumber}/history")
    public ResponseEntity<List<RoomHistoryResponse>> getRoomHistory(
            @PathVariable("roomNumber") int roomNumber) {

        List<RoomHistoryResponse> history = bookingService.getRoomHistory(roomNumber)
                .stream()
                .map(mapper::toHistoryResponse)
                .toList();

        return ResponseEntity.ok(history);
    }

    @PostMapping(value = "/check-in", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> checkIn(
            @Valid @RequestBody CheckInRequest request) {

        bookingService.checkIn(
                request.getGuestName(),
                request.getRoomNumber(),
                request.getCheckIn(),
                request.getCheckOut()
        );

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/check-out/{roomNumber}")
    public ResponseEntity<Void> checkOut(
            @PathVariable("roomNumber") int roomNumber) {

        bookingService.checkOut(roomNumber);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/export")
    public ResponseEntity<Void> exportCSV(
            @Valid @RequestBody CsvPathRequest request) {

        bookingService.exportBookingToCSV(request.getPath());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/import")
    public ResponseEntity<Void> importCSV(
            @Valid @RequestBody CsvPathRequest request) {

        bookingService.importBookingFromCSV(request.getPath());

        return ResponseEntity.ok().build();
    }
}
