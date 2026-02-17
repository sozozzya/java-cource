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
public class ImportBookingsAction implements IAction {

    private static final Logger log = LoggerFactory.getLogger(ImportBookingsAction.class);

    private final BookingManager bookingManager;
    private final ConsoleReader consoleReader;

    @Autowired
    public ImportBookingsAction(BookingManager bookingManager,
                                ConsoleReader consoleReader) {
        this.bookingManager = bookingManager;
        this.consoleReader = consoleReader;
    }

    @Override
    public void execute() {
        log.info("User command started: IMPORT_BOOKINGS");

        try {
            System.out.print("Enter CSV file path to import bookings: ");
            String path = consoleReader.nextLine();

            bookingManager.importBookingFromCSV(path);

            log.info("User command completed successfully: IMPORT_BOOKINGS, path={}", path);
            System.out.println("Bookings imported successfully.");
        } catch (BookingException e) {
            log.error("User command failed: IMPORT_BOOKINGS", e);
            System.out.println("Failed to import bookings: " + e.getMessage());
        }
    }
}
