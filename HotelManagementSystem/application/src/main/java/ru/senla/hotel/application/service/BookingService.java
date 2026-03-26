package ru.senla.hotel.application.service;

import ru.senla.hotel.domain.entity.Booking;
import ru.senla.hotel.domain.entity.Guest;
import ru.senla.hotel.domain.entity.Room;
import ru.senla.hotel.domain.entity.StayRecord;
import ru.senla.hotel.domain.enums.SortField;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface BookingService {

    List<Booking> getActiveBookings(SortField field);

    List<Guest> getCurrentGuests();

    long getCurrentGuestsCount();

    boolean isRoomFree(Room room, LocalDate checkIn, LocalDate checkOut);

    List<Room> getAvailableRooms(SortField field);

    long countAvailableRooms();

    List<Room> getRoomsFreeByDate(LocalDate date);

    List<StayRecord> getRoomHistory(int roomNumber);

    Booking addBooking(Booking booking);

    void addBookingsBatch(List<Booking> bookings);

    void checkIn(String guestName, int roomNumber,
                 LocalDate checkIn, LocalDate checkOut);

    void checkOut(int roomNumber);

    BigDecimal calculateGuestRoomCost(String guestName);

    BigDecimal calculateGuestServicesCost(String guestName);

    BigDecimal calculateGuestTotalCost(String guestName);

    void exportBookingToCSV(String path);

    void importBookingFromCSV(String path);
}
