package ru.senla.hotel.presentation.console.actions.bookings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.senla.hotel.service.BookingManager;
import ru.senla.hotel.service.exception.booking.BookingException;
import ru.senla.hotel.presentation.console.actions.IAction;
import ru.senla.hotel.presentation.console.util.ConsoleReader;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ExportBookingsAction implements IAction {

    private static final Logger log = LoggerFactory.getLogger(ExportBookingsAction.class);

    private final BookingManager bookingManager;
    private final ConsoleReader consoleReader;

    @Autowired
    public ExportBookingsAction(BookingManager bookingManager,
                                ConsoleReader consoleReader) {
        this.bookingManager = bookingManager;
        this.consoleReader = consoleReader;
    }

    @Override
    public void execute() {
        log.info("User command started: EXPORT_BOOKINGS");

        try {
            System.out.print("Enter CSV file path to export bookings: ");
            String path = consoleReader.nextLine();

            bookingManager.exportBookingToCSV(path);

            log.info("User command completed successfully: EXPORT_BOOKINGS, path={}", path);
            System.out.println("Bookings exported successfully.");
        } catch (BookingException e) {
            log.error("User command failed: EXPORT_BOOKINGS", e);
            System.out.println("Failed to export bookings: " + e.getMessage());
        }
    }
}
