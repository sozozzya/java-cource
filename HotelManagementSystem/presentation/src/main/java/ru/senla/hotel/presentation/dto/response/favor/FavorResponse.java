package ru.senla.hotel.presentation.dto.response.favor;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FavorResponse {

    private Long id;
    private String name;
    private BigDecimal price;
}
