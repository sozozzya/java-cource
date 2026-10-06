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
import ru.senla.hotel.application.service.FavorService;
import ru.senla.hotel.domain.entity.Favor;
import ru.senla.hotel.domain.enums.SortField;
import ru.senla.hotel.presentation.dto.request.favor.ChangeFavorPriceRequest;
import ru.senla.hotel.presentation.dto.request.favor.CreateFavorRequest;
import ru.senla.hotel.presentation.dto.request.CsvPathRequest;
import ru.senla.hotel.presentation.dto.response.favor.FavorPriceResponse;
import ru.senla.hotel.presentation.dto.response.favor.FavorResponse;
import ru.senla.hotel.presentation.mapper.FavorMapper;

import java.util.List;

@RestController
@RequestMapping(value = "/api/favors", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAnyRole('USER','ADMIN')")
public class FavorController {

    private final FavorService favorService;
    private final FavorMapper mapper;

    public FavorController(FavorService favorService,
                           FavorMapper mapper) {
        this.favorService = favorService;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<FavorResponse>> getAllFavors(
            @RequestParam(name = "sortField", required = false) SortField field) {

        List<FavorResponse> favors = favorService.getAvailableFavors(field)
                .stream()
                .map(mapper::toResponse)
                .toList();

        return ResponseEntity.ok(favors);
    }

    @GetMapping("/prices")
    public ResponseEntity<List<FavorPriceResponse>> getFavorPrices() {

        List<FavorPriceResponse> prices = favorService.getAvailableFavors(SortField.PRICE)
                .stream()
                .map(mapper::toPriceResponse)
                .toList();

        return ResponseEntity.ok(prices);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FavorResponse> createFavor(
            @Valid @RequestBody CreateFavorRequest request) {

        Favor saved = favorService.addFavor(
                mapper.toEntity(request)
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapper.toResponse(saved));
    }

    @PatchMapping("/{serviceName}/price")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> changeFavorPrice(
            @PathVariable("serviceName") String serviceName,
            @Valid @RequestBody ChangeFavorPriceRequest request) {

        favorService.changeFavorPrice(
                serviceName,
                request.getPrice()
        );

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/export")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> exportCSV(
            @Valid @RequestBody CsvPathRequest request) {

        favorService.exportFavorToCSV(request.getPath());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/import")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> importCSV(
            @Valid @RequestBody CsvPathRequest request) {

        favorService.importFavorFromCSV(request.getPath());

        return ResponseEntity.ok().build();
    }
}
