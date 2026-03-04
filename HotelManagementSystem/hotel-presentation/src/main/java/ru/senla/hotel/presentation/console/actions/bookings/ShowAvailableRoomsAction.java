package ru.senla.hotel.presentation.console.actions.bookings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.senla.hotel.presentation.console.ConsoleReportPrinter;
import ru.senla.hotel.service.exception.booking.BookingException;
import ru.senla.hotel.presentation.console.actions.IAction;
import ru.senla.hotel.service.util.SortUtils;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ShowAvailableRoomsAction implements IAction {

    private static final Logger log = LoggerFactory.getLogger(ShowAvailableRoomsAction.class);

    private final ConsoleReportPrinter consoleReportPrinter;
    private String sortBy;

    @Autowired
    public ShowAvailableRoomsAction(ConsoleReportPrinter consoleReportPrinter) {
        this.consoleReportPrinter = consoleReportPrinter;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    @Override
    public void execute() {
        log.info("User command started: SHOW_AVAILABLE_ROOMS, sortBy={}", sortBy);

        try {
            switch (sortBy) {
                case "capacity" -> consoleReportPrinter.printAvailableRooms(SortUtils.byRoomCapacity());
                case "stars" -> consoleReportPrinter.printAvailableRooms(SortUtils.byRoomStars());
                default -> consoleReportPrinter.printAvailableRooms(SortUtils.byRoomPrice());
            }

            log.info("User command completed successfully: SHOW_AVAILABLE_ROOMS, sortBy={}", sortBy);
        } catch (BookingException e) {
            log.error("User command failed: SHOW_AVAILABLE_ROOMS, sortBy={}", sortBy, e);
            System.out.println("Failed to show guests: " + e.getMessage());
        }
    }
}
