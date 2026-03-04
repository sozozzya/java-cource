package ru.senla.hotel.repository;

import ru.senla.hotel.repository.exception.RepositoryException;
import ru.senla.hotel.domain.model.BookingService;

import java.util.List;

public interface BookingServiceRepository extends GenericRepository<BookingService, Long> {

    List<BookingService> findServicesByGuestName(String guestName);
}
