package ru.senla.hotel.presentation.dto.request.guest;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateGuestRequest {

    @NotBlank
    private String name;
}
