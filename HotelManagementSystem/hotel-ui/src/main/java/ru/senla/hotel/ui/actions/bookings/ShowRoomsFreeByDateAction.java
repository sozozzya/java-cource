package ru.senla.hotel.ui.actions.bookings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.di.annotation.Scope;
import ru.senla.hotel.service.Administrator;
import ru.senla.hotel.service.exception.booking.BookingException;
import ru.senla.hotel.ui.menu.IAction;
import ru.senla.hotel.ui.util.ConsoleReader;

import java.time.LocalDate;

@Component(scope = Scope.PROTOTYPE)
public class ShowRoomsFreeByDateAction implements IAction {

    @Inject
    private Administrator admin;

    @Inject
    private ConsoleReader reader;

    private static final Logger log = LoggerFactory.getLogger(ShowRoomsFreeByDateAction.class);

    @Override
    public void execute() {
        log.info("User command started: SHOW_ROOMS_FREE_BY_DATE");

        try {
            System.out.print("Enter date (YYYY-MM-DD): ");
            LocalDate date = LocalDate.parse(reader.nextLine());

            admin.printRoomsFreeByDate(date);

            log.info("User command completed successfully: SHOW_ROOMS_FREE_BY_DATE");
        } catch (BookingException e) {
            log.error("User command failed: SHOW_ROOMS_FREE_BY_DATE", e);
            System.out.println("Failed to get rooms free by date: " + e.getMessage());
        }
    }
}
