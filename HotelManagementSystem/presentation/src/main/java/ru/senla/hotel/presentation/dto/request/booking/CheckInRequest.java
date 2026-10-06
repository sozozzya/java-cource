package ru.senla.hotel.presentation.dto.request.booking;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class CheckInRequest {

    @NotBlank
    private String guestName;

    @NotNull
    private int roomNumber;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkIn;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkOut;
}