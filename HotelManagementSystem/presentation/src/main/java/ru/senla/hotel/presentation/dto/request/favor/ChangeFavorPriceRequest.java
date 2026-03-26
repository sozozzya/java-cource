package ru.senla.hotel.presentation.dto.request.favor;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ChangeFavorPriceRequest {

    private BigDecimal price;
}
