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
import ru.senla.hotel.presentation.console.util.ConsoleReader;

import java.time.LocalDate;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ShowRoomsFreeByDateAction implements IAction {

    private static final Logger log = LoggerFactory.getLogger(ShowRoomsFreeByDateAction.class);

    private final ConsoleReportPrinter consoleReportPrinter;
    private final ConsoleReader consoleReader;

    @Autowired
    public ShowRoomsFreeByDateAction(ConsoleReportPrinter consoleReportPrinter,
                                     ConsoleReader consoleReader) {
        this.consoleReportPrinter = consoleReportPrinter;
        this.consoleReader = consoleReader;
    }

    @Override
    public void execute() {
        log.info("User command started: SHOW_ROOMS_FREE_BY_DATE");

        try {
            System.out.print("Enter date (YYYY-MM-DD): ");
            LocalDate date = LocalDate.parse(consoleReader.nextLine());

            consoleReportPrinter.printRoomsFreeByDate(date);

            log.info("User command completed successfully: SHOW_ROOMS_FREE_BY_DATE");
        } catch (BookingException e) {
            log.error("User command failed: SHOW_ROOMS_FREE_BY_DATE", e);
            System.out.println("Failed to get rooms free by date: " + e.getMessage());
        }
    }
}
