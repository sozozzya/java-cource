package ru.senla.hotel.ui.actions.bookings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.di.annotation.Scope;
import ru.senla.hotel.service.exception.booking.BookingException;
import ru.senla.hotel.service.exception.room.RoomException;
import ru.senla.hotel.service.Administrator;
import ru.senla.hotel.ui.menu.IAction;
import ru.senla.hotel.ui.util.ConsoleReader;

@Component(scope = Scope.PROTOTYPE)
public class CheckOutAction implements IAction {

    @Inject
    private Administrator admin;

    @Inject
    private ConsoleReader reader;

    private static final Logger log = LoggerFactory.getLogger(CheckOutAction.class);

    @Override
    public void execute() {
        log.info("User command started: CHECK_OUT");

        try {
            System.out.print("Enter room number to check out: ");
            int roomNumber = reader.nextInt();

            admin.checkOut(roomNumber);
            System.out.println("Guest successfully checked out.");

            log.info("User command completed successfully: CHECK_OUT");
        } catch (BookingException | RoomException e) {
            log.error("User command failed: CHECK_OUT", e);
            System.out.println("Check-out failed: " + e.getMessage());
        }
    }
}
