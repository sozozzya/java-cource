package ru.senla.hotel.presentation.mapper;

import org.springframework.stereotype.Component;
import ru.senla.hotel.domain.entity.Booking;
import ru.senla.hotel.domain.entity.Guest;
import ru.senla.hotel.presentation.dto.request.guest.CreateGuestRequest;
import ru.senla.hotel.presentation.dto.response.guest.GuestPaymentResponse;
import ru.senla.hotel.presentation.dto.response.guest.GuestResponse;
import ru.senla.hotel.presentation.dto.response.guest.GuestRoomResponse;

import java.math.BigDecimal;

@Component
public class GuestMapper {

    public GuestResponse toResponse(Guest guest) {

        GuestResponse dto = new GuestResponse();

        dto.setId(guest.getId());
        dto.setName(guest.getName());

        return dto;
    }

    public GuestRoomResponse toGuestRoomResponse(Booking booking) {

        GuestRoomResponse dto = new GuestRoomResponse();

        dto.setGuestName(booking.getGuest().getName());
        dto.setRoomNumber(booking.getRoom().getNumber());
        dto.setCheckInDate(booking.getCheckInDate());
        dto.setCheckOutDate(booking.getCheckOutDate());

        return dto;
    }

    public GuestPaymentResponse toPaymentResponse(String guestName,
                                                  BigDecimal amount) {

        GuestPaymentResponse dto = new GuestPaymentResponse();

        dto.setGuestName(guestName);
        dto.setTotalAmount(amount);

        return dto;
    }

    public Guest toEntity(CreateGuestRequest request) {

        Guest guest = new Guest();
        guest.setName(request.getName());

        return guest;
    }
}
