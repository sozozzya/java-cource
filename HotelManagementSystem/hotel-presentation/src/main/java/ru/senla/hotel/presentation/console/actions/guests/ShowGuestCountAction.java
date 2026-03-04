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

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ShowGuestCountAction implements IAction {

    private static final Logger log = LoggerFactory.getLogger(ShowGuestCountAction.class);

    private final ConsoleReportPrinter consoleReportPrinter;

    @Autowired
    public ShowGuestCountAction(ConsoleReportPrinter consoleReportPrinter) {
        this.consoleReportPrinter = consoleReportPrinter;
    }

    @Override
    public void execute() {
        log.info("User command started: SHOW_GUEST_COUNT");

        try {
            consoleReportPrinter.printGuestCount();

            log.info("User command completed successfully: SHOW_GUEST_COUNT");
        } catch (GuestException e) {
            log.error("User command failed: SHOW_GUEST_COUNT", e);
            System.out.println("Failed to show guest count: " + e.getMessage());
        }
    }
}
