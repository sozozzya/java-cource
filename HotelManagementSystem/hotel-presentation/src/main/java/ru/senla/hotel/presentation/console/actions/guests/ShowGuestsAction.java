package ru.senla.hotel.presentation.console.actions.guests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.senla.hotel.presentation.console.ConsoleReportPrinter;
import ru.senla.hotel.service.exception.guest.GuestException;
import ru.senla.hotel.presentation.console.actions.IAction;
import ru.senla.hotel.service.util.SortUtils;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ShowGuestsAction implements IAction {

    private static final Logger log = LoggerFactory.getLogger(ShowGuestsAction.class);

    private final ConsoleReportPrinter consoleReportPrinter;
    private String sortBy;

    @Autowired
    public ShowGuestsAction(ConsoleReportPrinter consoleReportPrinter) {
        this.consoleReportPrinter = consoleReportPrinter;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    @Override
    public void execute() {
        log.info("User command started: SHOW_GUESTS, sortBy={}", sortBy);

        try {
            if ("name".equals(sortBy)) {
                consoleReportPrinter.printCurrentGuests(SortUtils.byGuestName());
            } else {
                consoleReportPrinter.printCurrentGuests(SortUtils.byGuestCheckOutDate());
            }

            log.info("User command completed successfully: SHOW_GUESTS, sortBy={}", sortBy);
        } catch (GuestException e) {
            log.error("User command failed: SHOW_GUESTS, sortBy={}", sortBy, e);
            System.out.println("Failed to show guests: " + e.getMessage());
        }
    }
}

