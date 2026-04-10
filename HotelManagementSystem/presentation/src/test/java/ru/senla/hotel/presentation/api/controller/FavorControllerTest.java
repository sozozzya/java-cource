package ru.senla.hotel.presentation.api.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.senla.hotel.application.service.FavorService;
import ru.senla.hotel.domain.entity.Favor;
import ru.senla.hotel.domain.enums.SortField;
import ru.senla.hotel.presentation.dto.request.CsvPathRequest;
import ru.senla.hotel.presentation.dto.request.favor.ChangeFavorPriceRequest;
import ru.senla.hotel.presentation.dto.request.favor.CreateFavorRequest;
import ru.senla.hotel.presentation.dto.response.favor.FavorPriceResponse;
import ru.senla.hotel.presentation.dto.response.favor.FavorResponse;
import ru.senla.hotel.presentation.mapper.FavorMapper;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FavorControllerTest {

    @Mock
    private FavorService favorService;
    @Mock
    private FavorMapper mapper;
    @InjectMocks
    private FavorController controller;

    @Test
    void getAllFavorsShouldReturnMappedListWhenServiceResponds() {
        Favor favor = mock(Favor.class);
        FavorResponse favorResponse = mock(FavorResponse.class);
        when(favorService.getAvailableFavors(SortField.NAME)).thenReturn(List.of(favor));
        when(mapper.toResponse(favor)).thenReturn(favorResponse);

        ResponseEntity<List<FavorResponse>> response = controller.getAllFavors(SortField.NAME);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getAllFavorsShouldPropagateExceptionWhenServiceFails() {
        when(favorService.getAvailableFavors(null)).thenThrow(new RuntimeException("boom"));

        assertThrows(RuntimeException.class, () -> controller.getAllFavors(null));
    }

    @Test
    void getFavorPricesShouldReturnMappedPriceListWhenServiceResponds() {
        Favor favor = mock(Favor.class);
        FavorPriceResponse priceResponse = mock(FavorPriceResponse.class);
        when(favorService.getAvailableFavors(SortField.PRICE)).thenReturn(List.of(favor));
        when(mapper.toPriceResponse(favor)).thenReturn(priceResponse);

        ResponseEntity<List<FavorPriceResponse>> response = controller.getFavorPrices();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getFavorPricesShouldPropagateExceptionWhenServiceFails() {
        when(favorService.getAvailableFavors(SortField.PRICE)).thenThrow(new RuntimeException("boom"));

        assertThrows(RuntimeException.class, controller::getFavorPrices);
    }

    @Test
    void createFavorShouldReturnCreatedWhenInputIsValid() {
        CreateFavorRequest request = mock(CreateFavorRequest.class);
        Favor toSave = mock(Favor.class);
        Favor saved = mock(Favor.class);
        FavorResponse responseDto = mock(FavorResponse.class);

        when(mapper.toEntity(request)).thenReturn(toSave);
        when(favorService.addFavor(toSave)).thenReturn(saved);
        when(mapper.toResponse(saved)).thenReturn(responseDto);

        ResponseEntity<FavorResponse> response = controller.createFavor(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseDto, response.getBody());
    }

    @Test
    void createFavorShouldPropagateExceptionWhenServiceFails() {
        CreateFavorRequest request = mock(CreateFavorRequest.class);
        Favor toSave = mock(Favor.class);
        when(mapper.toEntity(request)).thenReturn(toSave);
        when(favorService.addFavor(toSave)).thenThrow(new RuntimeException("exists"));

        assertThrows(RuntimeException.class, () -> controller.createFavor(request));
    }

    @Test
    void changeFavorPriceShouldReturnNoContentWhenUpdateSucceeds() {
        ChangeFavorPriceRequest request = mock(ChangeFavorPriceRequest.class);
        when(request.getPrice()).thenReturn(BigDecimal.TEN);

        ResponseEntity<Void> response = controller.changeFavorPrice("Spa", request);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(favorService).changeFavorPrice("Spa", BigDecimal.TEN);
    }

    @Test
    void changeFavorPriceShouldPropagateExceptionWhenServiceFails() {
        ChangeFavorPriceRequest request = mock(ChangeFavorPriceRequest.class);
        when(request.getPrice()).thenReturn(BigDecimal.ONE);
        doThrow(new RuntimeException("invalid"))
                .when(favorService).changeFavorPrice("Spa", BigDecimal.ONE);

        assertThrows(RuntimeException.class, () -> controller.changeFavorPrice("Spa", request));
    }

    @Test
    void exportCsvShouldReturnOkWhenExportSucceeds() {
        CsvPathRequest request = mock(CsvPathRequest.class);
        when(request.getPath()).thenReturn("/tmp/favors.csv");

        ResponseEntity<Void> response = controller.exportCSV(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(favorService).exportFavorToCSV("/tmp/favors.csv");
    }

    @Test
    void exportCsvShouldPropagateExceptionWhenExportFails() {
        CsvPathRequest request = mock(CsvPathRequest.class);
        when(request.getPath()).thenReturn("/tmp/favors.csv");
        doThrow(new RuntimeException("io"))
                .when(favorService).exportFavorToCSV("/tmp/favors.csv");

        assertThrows(RuntimeException.class, () -> controller.exportCSV(request));
    }

    @Test
    void importCsvShouldReturnOkWhenImportSucceeds() {
        CsvPathRequest request = mock(CsvPathRequest.class);
        when(request.getPath()).thenReturn("/tmp/favors.csv");

        ResponseEntity<Void> response = controller.importCSV(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(favorService).importFavorFromCSV("/tmp/favors.csv");
    }

    @Test
    void importCsvShouldPropagateExceptionWhenImportFails() {
        CsvPathRequest request = mock(CsvPathRequest.class);
        when(request.getPath()).thenReturn("/tmp/favors.csv");
        doThrow(new RuntimeException("io"))
                .when(favorService).importFavorFromCSV("/tmp/favors.csv");

        assertThrows(RuntimeException.class, () -> controller.importCSV(request));
    }
}
