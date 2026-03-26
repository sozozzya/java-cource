package ru.senla.hotel.infrastructure.repository;

import ru.senla.hotel.domain.entity.BookingFavor;

import java.util.List;

public interface BookingFavorRepository extends GenericRepository<BookingFavor, Long> {

    List<BookingFavor> findFavorsByGuestName(String guestName);
}
