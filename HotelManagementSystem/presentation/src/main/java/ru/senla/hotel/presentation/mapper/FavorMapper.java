package ru.senla.hotel.presentation.mapper;

import org.springframework.stereotype.Component;
import ru.senla.hotel.domain.entity.BookingFavor;
import ru.senla.hotel.domain.entity.Favor;
import ru.senla.hotel.presentation.dto.request.favor.CreateFavorRequest;
import ru.senla.hotel.presentation.dto.response.favor.FavorPriceResponse;
import ru.senla.hotel.presentation.dto.response.favor.FavorResponse;
import ru.senla.hotel.presentation.dto.response.favor.BookingFavorResponse;

@Component
public class FavorMapper {

    public FavorResponse toResponse(Favor favor) {

        FavorResponse dto = new FavorResponse();

        dto.setId(favor.getId());
        dto.setName(favor.getName());
        dto.setPrice(favor.getPrice());

        return dto;
    }

    public FavorPriceResponse toPriceResponse(Favor favor) {

        FavorPriceResponse dto = new FavorPriceResponse();

        dto.setName(favor.getName());
        dto.setPrice(favor.getPrice());

        return dto;
    }

    public Favor toEntity(CreateFavorRequest request) {

        Favor favor = new Favor();

        favor.setName(request.getName());
        favor.setPrice(request.getPrice());

        return favor;
    }

    public BookingFavorResponse toGuestFavorResponse(BookingFavor bookingFavor) {

        BookingFavorResponse dto = new BookingFavorResponse();

        dto.setServiceName(bookingFavor.getFavor().getName());
        dto.setPrice(bookingFavor.getPrice());
        dto.setDate(bookingFavor.getDate());

        return dto;
    }
}
