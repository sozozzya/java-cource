package ru.senla.hotel.domain.entity;

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

    public String getGuestName() {
        return guestName;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    @Override
    public String toString() {
        return guestName + " | Check-in: " + checkIn + " | Check-out: " + checkOut;
    }
}
