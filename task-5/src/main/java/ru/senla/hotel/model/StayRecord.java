package ru.senla.hotel.model;

import java.time.LocalDate;

public class StayRecord {
    private final String guestName;
    private final LocalDate checkIn;
    private final LocalDate checkOut;

    public StayRecord(String guestName, LocalDate checkIn, LocalDate checkOut) {
        this.guestName = guestName;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    @Override
    public String toString() {
        return guestName + " | Check-in: " + checkIn + " | Check-out: " + checkOut;
    }
}
