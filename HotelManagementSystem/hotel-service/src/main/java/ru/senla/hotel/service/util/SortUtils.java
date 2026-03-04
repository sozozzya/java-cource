package ru.senla.hotel.service.util;

import ru.senla.hotel.domain.model.Booking;
import ru.senla.hotel.domain.model.BookingService;
import ru.senla.hotel.domain.model.Room;

import java.util.Comparator;

public class SortUtils {
    public static Comparator<Room> byRoomPrice() {
        return Comparator.comparing(Room::getPricePerNight);
    }

    public static Comparator<Room> byRoomCapacity() {
        return Comparator.comparing(Room::getCapacity);
    }

    public static Comparator<Room> byRoomStars() {
        return Comparator.comparing(Room::getStars);
    }

    public static Comparator<Booking> byGuestName() {
        return Comparator.comparing(
                b -> b.getGuest().getName(),
                String.CASE_INSENSITIVE_ORDER
        );
    }

    public static Comparator<Booking> byGuestCheckOutDate() {
        return Comparator.comparing(Booking::getCheckOutDate);
    }

    public static Comparator<BookingService> byServicePrice() {
        return Comparator.comparing(BookingService::getPrice);
    }

    public static Comparator<BookingService> byServiceDate() {
        return Comparator.comparing(BookingService::getDate);
    }
}
