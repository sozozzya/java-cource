package ru.senla.hotel.domain.model;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

public record StayRecord(
        String guestName,
        LocalDate checkIn,
        LocalDate checkOut
) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return guestName + " | Check-in: " + checkIn + " | Check-out: " + checkOut;
    }
}
