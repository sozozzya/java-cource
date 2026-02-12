package ru.senla.hotel.ui.actions.bookings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.service.Administrator;
import ru.senla.hotel.service.exception.booking.BookingException;
import ru.senla.hotel.ui.menu.IAction;

public class ShowAvailableRoomsAction implements IAction {

    @Inject
    private Administrator admin;

    private final String by;

    public ShowAvailableRoomsAction(String by) {
        this.by = by;
    }

    private static final Logger log = LoggerFactory.getLogger(ShowAvailableRoomsAction.class);

    @Override
    public void execute() {
        log.info("User command started: SHOW_AVAILABLE_ROOMS, by={}", by);

        try {
            switch (by) {
                case "capacity" -> admin.printAvailableRoomsByCapacity();
                case "stars" -> admin.printAvailableRoomsByStars();
                default -> admin.printAvailableRoomsByPrice();
            }

            log.info("User command completed successfully: SHOW_AVAILABLE_ROOMS, by={}", by);
        } catch (BookingException e) {
            log.error("User command failed: SHOW_AVAILABLE_ROOMS, by={}", by, e);
            System.out.println("Failed to show guests: " + e.getMessage());
        }
    }
}
