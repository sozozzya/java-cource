package ru.senla.hotel.service;

import ru.senla.hotel.domain.model.BookingService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface BookingServiceManager {

    List<BookingService> getServicesByGuest(String guestName);

    BigDecimal assignServiceToGuest(String guestName,
                                    String serviceName,
                                    LocalDate date);
}
