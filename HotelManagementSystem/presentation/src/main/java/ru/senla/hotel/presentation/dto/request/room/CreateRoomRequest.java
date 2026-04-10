package ru.senla.hotel.presentation.dto.request.room;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateRoomRequest {

    @NotNull
    private int number;

    @NotNull
    @DecimalMin("0.0")
    private int capacity;

    @NotNull
    @DecimalMin("0.0")
    private int stars;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal pricePerNight;
}
