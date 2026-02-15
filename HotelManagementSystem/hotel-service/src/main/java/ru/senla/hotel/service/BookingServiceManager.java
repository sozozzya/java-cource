package ru.senla.hotel.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.senla.hotel.dao.BookingDAO;
import ru.senla.hotel.dao.BookingServiceDAO;
import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.model.Booking;
import ru.senla.hotel.model.BookingService;
import ru.senla.hotel.model.Guest;
import ru.senla.hotel.model.Service;
import ru.senla.hotel.service.exception.booking.BookingException;
import ru.senla.hotel.service.exception.booking.NoActiveBookingException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class BookingServiceManager {

    @Inject
    private BookingDAO bookingDAO;

    @Inject
    private BookingServiceDAO bookingServiceDAO;

    @Inject
    private ServiceManager serviceManager;

    @Inject
    private GuestManager guestManager;

    private static final Logger log = LoggerFactory.getLogger(BookingServiceManager.class);

    public List<BookingService> getServicesByGuest(String guestName) {
        log.info("Fetching services for guest='{}'", guestName);
        return bookingServiceDAO.findServicesByGuestName(guestName);
    }

    public BigDecimal assignServiceToGuest(String guestName,
                                           String serviceName,
                                           LocalDate date) {
        log.info("Assigning service to guest: guest='{}', service='{}', date={}", guestName, serviceName, date);
        try {
            Guest guest = guestManager.getGuestByName(guestName);

            Booking booking = bookingDAO.findByGuestId(guest.getId()).stream()
                    .filter(b ->
                            !date.isBefore(b.getCheckInDate()) &&
                                    !date.isAfter(b.getCheckOutDate())
                    )
                    .findFirst()
                    .orElseThrow(() -> new NoActiveBookingException(guestName));

            Service service = serviceManager.getServiceByName(serviceName);
            BookingService bookingService = new BookingService(booking, service, date);

            bookingServiceDAO.save(bookingService);
            log.info("Service successfully assigned: guest='{}', service='{}'", guestName, serviceName);
            return service.getPrice();
        } catch (Exception e) {
            log.error("Failed to assign service '{}' to guest='{}'", serviceName, guestName, e);
            throw new BookingException("Failed to assign service to guest", e);
        }
    }
}
