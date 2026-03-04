package ru.senla.hotel.service.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.senla.hotel.repository.BookingRepository;
import ru.senla.hotel.repository.BookingServiceRepository;
import ru.senla.hotel.domain.model.Booking;
import ru.senla.hotel.domain.model.BookingService;
import ru.senla.hotel.domain.model.Guest;
import ru.senla.hotel.domain.model.HotelService;
import ru.senla.hotel.service.BookingServiceManager;
import ru.senla.hotel.service.GuestManager;
import ru.senla.hotel.service.ServiceManager;
import ru.senla.hotel.service.exception.booking.BookingException;
import ru.senla.hotel.service.exception.booking.NoActiveBookingException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class BookingServiceManagerImpl implements BookingServiceManager {

    private static final Logger log = LoggerFactory.getLogger(BookingServiceManagerImpl.class);

    private final BookingRepository bookingRepository;
    private final BookingServiceRepository bookingServiceRepository;
    private final ServiceManager serviceManager;
    private final GuestManager guestManager;

    public BookingServiceManagerImpl(BookingRepository bookingRepository,
                                     BookingServiceRepository bookingServiceRepository,
                                     ServiceManager serviceManager,
                                     GuestManager guestManager) {
        this.bookingRepository = bookingRepository;
        this.bookingServiceRepository = bookingServiceRepository;
        this.serviceManager = serviceManager;
        this.guestManager = guestManager;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingService> getServicesByGuest(String guestName) {
        log.debug("Fetching services for guest: {}", guestName);

        try {
            return bookingServiceRepository.findServicesByGuestName(guestName);
        } catch (Exception e) {
            log.error("Failed to fetch services for guest: {}", guestName, e);
            throw new BookingException("Failed to fetch guest services", e);
        }
    }

    @Override
    public BigDecimal assignServiceToGuest(String guestName,
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

            HotelService service = serviceManager.getServiceByName(serviceName);
            BookingService bookingService = new BookingService(booking, service, date);

            bookingServiceRepository.save(bookingService);
            log.info("Service successfully assigned: guest='{}', service='{}'", guestName, serviceName);
            return service.getPrice();
        } catch (Exception e) {
            log.error("Failed to assign service '{}' to guest='{}'", serviceName, guestName, e);
            throw new BookingException("Failed to assign service to guest", e);
        }
    }
}
