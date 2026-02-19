package ru.senla.hotel.service;

import org.springframework.transaction.annotation.Transactional;
import ru.senla.hotel.domain.model.Booking;
import ru.senla.hotel.domain.model.Guest;
import ru.senla.hotel.domain.model.Room;
import ru.senla.hotel.domain.model.StayRecord;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface BookingManager {

    List<Booking> getActiveBookings();

    List<Guest> getCurrentGuests();

    long getCurrentGuestsCount();

    boolean isRoomFree(Room room, LocalDate checkIn, LocalDate checkOut);

    List<Room> getAvailableRooms();

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
