package ru.senla.hotel.presentation.dto.request.favor;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class CreateFavorRequest {

    @NotBlank
    private String name;

    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal price;
}
