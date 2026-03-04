package ru.senla.hotel.service.exception.booking;

import java.time.LocalDate;

public class InvalidBookingDatesException extends BookingException {
    public InvalidBookingDatesException(LocalDate in, LocalDate out) {
        super("Invalid dates: check-out (" + out + ") cannot be before check-in (" + in + ").");
    }
}
