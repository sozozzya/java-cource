package ru.senla.hotel.presentation.dto.response.room;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class RoomDetailsResponse {

    private Long id;
    private int number;
    private int capacity;
    private int stars;
    private BigDecimal pricePerNight;
    private boolean underMaintenance;
}
