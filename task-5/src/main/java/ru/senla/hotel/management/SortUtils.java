package ru.senla.hotel.management;

import ru.senla.hotel.model.*;

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

    public static Comparator<Guest> byGuestName() {
        return Comparator.comparing(Guest::getName, String.CASE_INSENSITIVE_ORDER);
    }

    public static Comparator<Guest> byGuestCheckOutDate() {
        return Comparator.comparing(Guest::getCheckOutDate);
    }

    public static Comparator<Service> byServicePrice() {
        return Comparator.comparing(Service::getPrice);
    }

    public static Comparator<Service> byServiceDate() {
        return Comparator.comparing(Service::getDate);
    }
}
