package ru.senla.hotel.model;

import java.time.LocalDate;

public record StayRecord(String guestName, LocalDate checkIn, LocalDate checkOut) {

    @Override
    public String toString() {
        return guestName + " | Check-in: " + checkIn + " | Check-out: " + checkOut;
    }
}
