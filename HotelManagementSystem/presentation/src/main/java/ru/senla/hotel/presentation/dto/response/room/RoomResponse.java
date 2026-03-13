package ru.senla.hotel.presentation.dto.response.room;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoomResponse {

    private Long id;
    private int number;
    private int capacity;
    private int stars;
    private BigDecimal pricePerNight;
    private boolean underMaintenance;
}