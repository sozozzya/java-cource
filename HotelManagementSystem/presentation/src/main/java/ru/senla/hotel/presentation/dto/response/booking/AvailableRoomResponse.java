package ru.senla.hotel.presentation.dto.response.booking;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AvailableRoomResponse {

    private int number;
    private int capacity;
    private int stars;
    private BigDecimal price;
}
