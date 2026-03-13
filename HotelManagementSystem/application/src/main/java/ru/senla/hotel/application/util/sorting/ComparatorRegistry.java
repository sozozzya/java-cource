package ru.senla.hotel.application.util.sorting;

import org.springframework.stereotype.Component;
import ru.senla.hotel.domain.entity.Booking;
import ru.senla.hotel.domain.entity.BookingFavor;
import ru.senla.hotel.domain.entity.Favor;
import ru.senla.hotel.domain.entity.Room;
import ru.senla.hotel.domain.enums.SortField;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.Map;

@Component
public class ComparatorRegistry {

    private final Map<SortField, Comparator<Room>> roomComparators = new EnumMap<>(SortField.class);
    private final Map<SortField, Comparator<Booking>> bookingComparators = new EnumMap<>(SortField.class);
    private final Map<SortField, Comparator<Favor>> favorComparators = new EnumMap<>(SortField.class);
    private final Map<SortField, Comparator<BookingFavor>> bookingFavorComparators = new EnumMap<>(SortField.class);

    public ComparatorRegistry() {
        roomComparators.put(SortField.PRICE, Comparator.comparing(Room::getPricePerNight, Comparator.nullsLast(BigDecimal::compareTo)));
        roomComparators.put(SortField.CAPACITY, Comparator.comparingInt(Room::getCapacity));
        roomComparators.put(SortField.STARS, Comparator.comparingInt(Room::getStars));

        bookingComparators.put(SortField.NAME,
                Comparator.comparing(b -> b.getGuest() != null ? b.getGuest().getName() : "",
                        String.CASE_INSENSITIVE_ORDER));
        bookingComparators.put(SortField.CHECKOUT_DATE,
                Comparator.comparing(b -> b.getCheckOutDate(), Comparator.nullsLast(LocalDate::compareTo)));

        favorComparators.put(SortField.PRICE, Comparator.comparing(Favor::getPrice, Comparator.nullsLast(BigDecimal::compareTo)));
        favorComparators.put(SortField.NAME, Comparator.comparing(Favor::getName, String.CASE_INSENSITIVE_ORDER));

        bookingFavorComparators.put(SortField.PRICE, Comparator.comparing(BookingFavor::getPrice, Comparator.nullsLast(BigDecimal::compareTo)));
        bookingFavorComparators.put(SortField.DATE, Comparator.comparing(BookingFavor::getDate, Comparator.nullsLast(LocalDate::compareTo)));
    }

    public Comparator<Room> roomComparator(SortField field) {
        return roomComparators.getOrDefault(field, Comparator.comparing(Room::getId, Comparator.nullsLast(Long::compareTo)));
    }

    public Comparator<Booking> bookingComparator(SortField field) {
        return bookingComparators.getOrDefault(field, Comparator.comparing(Booking::getId, Comparator.nullsLast(Long::compareTo)));
    }

    public Comparator<Favor> favorComparator(SortField field) {
        return favorComparators.getOrDefault(field, Comparator.comparing(Favor::getId, Comparator.nullsLast(Long::compareTo)));
    }

    public Comparator<BookingFavor> bookingFavorComparator(SortField field) {
        return bookingFavorComparators.getOrDefault(field, Comparator.comparing(BookingFavor::getId, Comparator.nullsLast(Long::compareTo)));
    }
}
