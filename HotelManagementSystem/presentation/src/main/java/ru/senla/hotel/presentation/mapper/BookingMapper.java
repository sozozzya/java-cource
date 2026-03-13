package ru.senla.hotel.presentation.mapper;

import org.springframework.stereotype.Component;
import ru.senla.hotel.domain.entity.Booking;
import ru.senla.hotel.domain.entity.Room;
import ru.senla.hotel.domain.entity.StayRecord;
import ru.senla.hotel.presentation.dto.response.booking.AvailableRoomResponse;
import ru.senla.hotel.presentation.dto.response.booking.BookingResponse;
import ru.senla.hotel.presentation.dto.response.booking.RoomHistoryResponse;

@Component
public class BookingMapper {

    public BookingResponse toResponse(Booking booking) {

        BookingResponse dto = new BookingResponse();

        dto.setId(booking.getId());
        dto.setGuestName(booking.getGuest().getName());
        dto.setRoomNumber(booking.getRoom().getNumber());
        dto.setCheckIn(booking.getCheckInDate());
        dto.setCheckOut(booking.getCheckOutDate());

        return dto;
    }

    public AvailableRoomResponse toRoomResponse(Room room) {

        AvailableRoomResponse dto = new AvailableRoomResponse();

        dto.setNumber(room.getNumber());
        dto.setCapacity(room.getCapacity());
        dto.setStars(room.getStars());
        dto.setPrice(room.getPricePerNight());

        return dto;
    }

    public RoomHistoryResponse toHistoryResponse(StayRecord record) {

        RoomHistoryResponse dto = new RoomHistoryResponse();

        dto.setGuestName(record.guestName());
        dto.setCheckIn(record.checkIn());
        dto.setCheckOut(record.checkOut());

        return dto;
    }
}
