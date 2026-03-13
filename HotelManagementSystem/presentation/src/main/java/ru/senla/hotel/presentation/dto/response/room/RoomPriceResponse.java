package ru.senla.hotel.presentation.dto.response.room;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoomPriceResponse {

    private int roomNumber;
    private BigDecimal price;
}
