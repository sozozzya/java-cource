package ru.senla.hotel.application.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.senla.hotel.application.util.sorting.ComparatorRegistry;
import ru.senla.hotel.application.util.sorting.SortingService;
import ru.senla.hotel.domain.entity.Booking;
import ru.senla.hotel.domain.entity.BookingFavor;
import ru.senla.hotel.domain.entity.Favor;
import ru.senla.hotel.domain.entity.Guest;
import ru.senla.hotel.domain.enums.SortField;
import ru.senla.hotel.infrastructure.repository.BookingRepository;
import ru.senla.hotel.infrastructure.repository.BookingFavorRepository;
import ru.senla.hotel.application.service.BookingFavorService;
import ru.senla.hotel.application.service.GuestService;
import ru.senla.hotel.application.service.FavorService;
import ru.senla.hotel.application.exception.booking.BookingException;
import ru.senla.hotel.application.exception.booking.NoActiveBookingException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class BookingFavorServiceImpl implements BookingFavorService {

    private static final Logger log = LoggerFactory.getLogger(BookingFavorServiceImpl.class);

    private final BookingRepository bookingRepository;
    private final BookingFavorRepository bookingFavorRepository;
    private final FavorService favorService;
    private final GuestService guestManager;
    private final SortingService sortingService;
    private final ComparatorRegistry comparatorRegistry;

    public BookingFavorServiceImpl(BookingRepository bookingRepository,
                                   BookingFavorRepository bookingFavorRepository,
                                   FavorService favorService,
                                   GuestService guestManager,
                                   SortingService sortingService,
                                   ComparatorRegistry comparatorRegistry) {
        this.bookingRepository = bookingRepository;
        this.bookingFavorRepository = bookingFavorRepository;
        this.favorService = favorService;
        this.guestManager = guestManager;
        this.sortingService = sortingService;
        this.comparatorRegistry = comparatorRegistry;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingFavor> getGuestFavors(String guestName, SortField field) {
        log.debug("Fetching services for guest: {}", guestName);

        try {
            List<BookingFavor> favors = bookingFavorRepository.findFavorsByGuestName(guestName);

            return sortingService.sort(
                    favors,
                    comparatorRegistry.bookingFavorComparator(field)
            );
        } catch (Exception e) {
            log.error("Failed to fetch services for guest: {}", guestName, e);
            throw new BookingException("Failed to fetch guest services", e);
        }
    }

    @Override
    public BigDecimal assignFavorToGuest(String guestName,
                                         String serviceName,
                                         LocalDate date) {
        log.info("Assigning service to guest: guest='{}', service='{}', date={}", guestName, serviceName, date);
        try {
            Guest guest = guestManager.getGuestByName(guestName);

            Booking booking = bookingRepository.findByGuestId(guest.getId()).stream()
                    .filter(b ->
                            !date.isBefore(b.getCheckInDate()) &&
                                    !date.isAfter(b.getCheckOutDate())
                    )
                    .findFirst()
                    .orElseThrow(() -> {
                        log.warn("No active booking for guest {} on date {}", guestName, date);
                        return new NoActiveBookingException(guestName);
                    });

            Favor favor = favorService.getFavorByName(serviceName);
            BookingFavor bookingFavor = new BookingFavor(booking, favor, date);

            bookingFavorRepository.save(bookingFavor);
            log.info("Service successfully assigned: guest='{}', service='{}'", guestName, serviceName);
            return favor.getPrice();
        } catch (Exception e) {
            log.error("Failed to assign service '{}' to guest='{}'", serviceName, guestName, e);
            throw new BookingException("Failed to assign service to guest", e);
        }
    }
}
