package ru.senla.hotel.presentation.dto.request.room;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChangeRoomPriceRequest {

    private BigDecimal price;
}
