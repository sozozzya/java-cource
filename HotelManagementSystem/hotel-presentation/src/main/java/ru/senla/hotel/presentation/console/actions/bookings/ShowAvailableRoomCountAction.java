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

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ShowAvailableRoomCountAction implements IAction {

    private static final Logger log = LoggerFactory.getLogger(ShowAvailableRoomCountAction.class);

    private final ConsoleReportPrinter consoleReportPrinter;

    @Autowired
    public ShowAvailableRoomCountAction(ConsoleReportPrinter consoleReportPrinter) {
        this.consoleReportPrinter = consoleReportPrinter;
    }

    @Override
    public void execute() {
        log.info("User command started: SHOW_AVAILABLE_ROOM_COUNT");

        try {
            consoleReportPrinter.printAvailableRoomsCount();

            log.info("User command completed successfully: SHOW_AVAILABLE_ROOM_COUNT");
        } catch (BookingException e) {
            log.error("User command failed: SHOW_AVAILABLE_ROOM_COUNT", e);
            System.out.println("Failed to show available room count: " + e.getMessage());
        }
    }
}