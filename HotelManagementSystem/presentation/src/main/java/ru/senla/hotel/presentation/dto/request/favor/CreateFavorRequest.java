package ru.senla.hotel.presentation.dto.request.favor;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class CreateFavorRequest {

    private String name;
    private BigDecimal price;
}
