package ru.senla.hotel.presentation.console.actions.bookings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.senla.hotel.service.BookingManager;
import ru.senla.hotel.service.exception.booking.BookingException;
import ru.senla.hotel.service.exception.room.RoomException;
import ru.senla.hotel.presentation.console.actions.IAction;
import ru.senla.hotel.presentation.console.util.ConsoleReader;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CheckOutAction implements IAction {

    private static final Logger log = LoggerFactory.getLogger(CheckOutAction.class);

    private final BookingManager bookingManager;
    private final ConsoleReader consoleReader;

    @Autowired
    public CheckOutAction(BookingManager bookingManager,
                          ConsoleReader consoleReader) {
        this.bookingManager = bookingManager;
        this.consoleReader = consoleReader;
    }

    @Override
    public void execute() {
        log.info("User command started: CHECK_OUT");

        try {
            System.out.print("Enter room number to check out: ");
            int roomNumber = consoleReader.nextInt();

            bookingManager.checkOut(roomNumber);
            System.out.println("Guest successfully checked out.");

            log.info("User command completed successfully: CHECK_OUT");
        } catch (BookingException | RoomException e) {
            log.error("User command failed: CHECK_OUT", e);
            System.out.println("Check-out failed: " + e.getMessage());
        }
    }
}
