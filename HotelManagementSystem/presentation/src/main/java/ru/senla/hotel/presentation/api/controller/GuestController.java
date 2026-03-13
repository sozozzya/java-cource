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
import ru.senla.hotel.application.service.BookingFavorService;
import ru.senla.hotel.application.service.BookingService;
import ru.senla.hotel.application.service.GuestService;
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

@RestController
@RequestMapping(value = "/api/guests", produces = MediaType.APPLICATION_JSON_VALUE)
public class GuestController {

    private final GuestService guestService;
    private final BookingService bookingService;
    private final GuestMapper guestMapper;
    private final BookingFavorService bookingFavorService;
    private final FavorMapper favorMapper;


    public GuestController(GuestService guestService,
                           BookingService bookingService,
                           GuestMapper guestMapper,
                           BookingFavorService bookingFavorService,
                           FavorMapper favorMapper) {
        this.guestService = guestService;
        this.bookingService = bookingService;
        this.guestMapper = guestMapper;
        this.bookingFavorService = bookingFavorService;
        this.favorMapper = favorMapper;
    }

    @GetMapping
    public ResponseEntity<List<GuestResponse>> getGuests() {

        List<GuestResponse> guests = guestService.getAllGuests()
                .stream()
                .map(guestMapper::toResponse)
                .toList();

        return ResponseEntity.ok(guests);
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<GuestRoomResponse>> getGuestsWithRooms(
            @RequestParam(name = "sortField", required = false) SortField field) {

        List<GuestRoomResponse> guests = bookingService.getActiveBookings(field)
                .stream()
                .map(guestMapper::toGuestRoomResponse)
                .toList();

        return ResponseEntity.ok(guests);
    }

    @GetMapping("/{guestName}/favors")
    public ResponseEntity<List<BookingFavorResponse>> getGuestFavors(
            @PathVariable("guestName") String guestName,
            @RequestParam(name = "sortField", required = false) SortField field) {

        List<BookingFavorResponse> favors = bookingFavorService
                .getGuestFavors(guestName, field)
                .stream()
                .map(favorMapper::toGuestFavorResponse)
                .toList();

        return ResponseEntity.ok(favors);
    }

    @GetMapping("/count")
    public ResponseEntity<GuestCountResponse> countGuests() {

        long count = bookingService.getCurrentGuestsCount();

        GuestCountResponse response = new GuestCountResponse();
        response.setTotalGuests(count);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{guestName}/payment")
    public ResponseEntity<GuestPaymentResponse> calculatePayment(
            @PathVariable("guestName") String guestName) {

        BigDecimal amount =
                bookingService.calculateGuestTotalCost(guestName);

        return ResponseEntity.ok(
                guestMapper.toPaymentResponse(guestName, amount)
        );
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GuestResponse> createGuest(
            @Valid @RequestBody CreateGuestRequest request) {

        Guest saved = guestService.addGuest(
                guestMapper.toEntity(request)
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(guestMapper.toResponse(saved));
    }

    @PostMapping("/export")
    public ResponseEntity<Void> exportCSV(
            @Valid @RequestBody CsvPathRequest request) {

        guestService.exportGuestToCSV(request.getPath());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/import")
    public ResponseEntity<Void> importCSV(
            @Valid @RequestBody CsvPathRequest request) {

        guestService.importGuestFromCSV(request.getPath());

        return ResponseEntity.ok().build();
    }
}
