package ru.senla.hotel.ui.actions.bookings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.di.annotation.Scope;
import ru.senla.hotel.service.Administrator;
import ru.senla.hotel.service.exception.booking.BookingException;
import ru.senla.hotel.ui.menu.IAction;

@Component(scope = Scope.PROTOTYPE)
public class ShowAvailableRoomCountAction implements IAction {

    @Inject
    private Administrator admin;

    private static final Logger log = LoggerFactory.getLogger(ShowAvailableRoomCountAction.class);

    @Override
    public void execute() {
        log.info("User command started: SHOW_AVAILABLE_ROOM_COUNT");

        try {
            admin.printAvailableRoomCount();

            log.info("User command completed successfully: SHOW_AVAILABLE_ROOM_COUNT");
        } catch (BookingException e) {
            log.error("User command failed: SHOW_AVAILABLE_ROOM_COUNT", e);
            System.out.println("Failed to show available room count: " + e.getMessage());
        }
    }
}