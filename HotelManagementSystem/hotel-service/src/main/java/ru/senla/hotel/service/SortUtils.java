package ru.senla.hotel.service;

import ru.senla.hotel.model.Booking;
import ru.senla.hotel.model.Room;
import ru.senla.hotel.model.Service;

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

    public static Comparator<Service> byServicePrice() {
        return Comparator.comparing(Service::getPrice);
    }

    public static Comparator<Service> byServiceDate() {
        return Comparator.comparing(Service::getDate);
    }
}
