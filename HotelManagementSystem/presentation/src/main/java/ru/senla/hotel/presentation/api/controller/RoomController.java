package ru.senla.hotel.presentation.api.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PatchMapping;
import ru.senla.hotel.application.service.BookingService;
import ru.senla.hotel.application.service.RoomService;
import ru.senla.hotel.domain.entity.Room;
import ru.senla.hotel.domain.enums.SortField;
import ru.senla.hotel.presentation.dto.request.room.ChangeMaintenanceStatusRequest;
import ru.senla.hotel.presentation.dto.request.room.ChangeRoomPriceRequest;
import ru.senla.hotel.presentation.dto.request.room.CreateRoomRequest;
import ru.senla.hotel.presentation.dto.request.CsvPathRequest;
import ru.senla.hotel.presentation.dto.response.room.RoomDetailsResponse;
import ru.senla.hotel.presentation.dto.response.room.RoomLastGuestsResponse;
import ru.senla.hotel.presentation.dto.response.room.RoomPriceResponse;
import ru.senla.hotel.presentation.dto.response.room.RoomResponse;
import ru.senla.hotel.presentation.mapper.RoomMapper;

import java.util.List;

@RestController
@RequestMapping(value = "/api/rooms", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAnyRole('USER','ADMIN')")
public class RoomController {

    private final RoomService roomService;
    private final BookingService bookingService;
    private final RoomMapper mapper;

    public RoomController(RoomService roomService,
                          BookingService bookingService,
                          RoomMapper mapper) {
        this.roomService = roomService;
        this.bookingService = bookingService;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<RoomResponse>> getAllRooms(
            @RequestParam(name = "sortField", required = false) SortField field) {

        List<RoomResponse> rooms = roomService.getAllRooms(field)
                .stream()
                .map(mapper::toResponse)
                .toList();

        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/{roomNumber}")
    public ResponseEntity<RoomDetailsResponse> getRoom(
            @PathVariable("roomNumber") int roomNumber) {

        Room room = roomService.getRoomByNumber(roomNumber);

        return ResponseEntity.ok(
                mapper.toDetails(room)
        );
    }

    @GetMapping("/prices")
    public ResponseEntity<List<RoomPriceResponse>> getRoomPrices() {

        List<RoomPriceResponse> prices = roomService.getAllRooms(SortField.PRICE)
                .stream()
                .map(mapper::toPriceResponse)
                .toList();

        return ResponseEntity.ok(prices);
    }

    @GetMapping("/{roomNumber}/last-guests")
    public ResponseEntity<List<RoomLastGuestsResponse>> getLastGuests(
            @PathVariable("roomNumber") int roomNumber) {

        List<RoomLastGuestsResponse> guests = bookingService.getRoomHistory(roomNumber)
                .stream()
                .map(mapper::toLastGuestsResponse)
                .toList();

        return ResponseEntity.ok(guests);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoomResponse> createRoom(
            @Valid @RequestBody CreateRoomRequest request) {

        Room saved = roomService.addRoom(
                mapper.toEntity(request)
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapper.toResponse(saved));
    }

    @PatchMapping("/{roomNumber}/price")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> changePrice(
            @PathVariable("roomNumber") int roomNumber,
            @Valid @RequestBody ChangeRoomPriceRequest request) {

        roomService.changeRoomPrice(
                roomNumber,
                request.getPrice()
        );

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{roomNumber}/maintenance")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> changeMaintenance(
            @PathVariable("roomNumber") int roomNumber,
            @Valid @RequestBody ChangeMaintenanceStatusRequest request) {

        roomService.changeMaintenanceStatus(
                roomNumber,
                request.isUnderMaintenance()
        );

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/export")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> exportCSV(
            @Valid @RequestBody CsvPathRequest request) {

        roomService.exportRoomToCSV(request.getPath());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/import")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> importCSV(
            @Valid @RequestBody CsvPathRequest request) {

        roomService.importRoomFromCSV(request.getPath());

        return ResponseEntity.ok().build();
    }
}
