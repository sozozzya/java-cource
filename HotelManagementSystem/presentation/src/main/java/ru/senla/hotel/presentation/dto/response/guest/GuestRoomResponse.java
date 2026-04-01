package ru.senla.hotel.presentation.dto.response.guest;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GuestRoomResponse {

    private String guestName;
    private int roomNumber;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkInDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkOutDate;
}
