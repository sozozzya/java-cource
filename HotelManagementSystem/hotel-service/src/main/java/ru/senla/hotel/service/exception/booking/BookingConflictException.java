package ru.senla.hotel.service.exception.booking;

import java.time.LocalDate;

public class BookingConflictException extends BookingException {

    public BookingConflictException(int roomNumber,
                                    LocalDate checkIn,
                                    LocalDate checkOut) {

        super("Booking conflict for room " + roomNumber +
                ": dates " + checkIn + " to " + checkOut + " overlap with existing booking.");
    }
}
