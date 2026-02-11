package ru.senla.hotel.exception.booking;

public class NoActiveBookingException extends BookingException {
    public NoActiveBookingException(String guestName) {
        super("No active booking found for guest " + guestName + ".");
    }
}
