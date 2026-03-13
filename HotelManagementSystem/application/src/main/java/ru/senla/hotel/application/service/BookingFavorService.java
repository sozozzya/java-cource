package ru.senla.hotel.application.service;

import ru.senla.hotel.domain.entity.BookingFavor;
import ru.senla.hotel.domain.enums.SortField;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface BookingFavorService {

    List<BookingFavor> getGuestFavors(String guestName, SortField field);

    BigDecimal assignFavorToGuest(String guestName,
                                  String favorName,
                                  LocalDate date);
}
