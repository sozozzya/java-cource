package ru.senla.hotel.presentation.dto.response.favor;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BookingFavorResponse {

    private String serviceName;
    private BigDecimal price;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
}
