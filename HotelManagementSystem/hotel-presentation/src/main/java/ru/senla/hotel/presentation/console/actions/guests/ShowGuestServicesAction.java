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
import ru.senla.hotel.presentation.console.util.ConsoleReader;
import ru.senla.hotel.service.util.SortUtils;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ShowGuestServicesAction implements IAction {

    private static final Logger log = LoggerFactory.getLogger(ShowGuestServicesAction.class);

    private final ConsoleReportPrinter consoleReportPrinter;
    private final ConsoleReader consoleReader;
    private String sortBy;

    @Autowired
    public ShowGuestServicesAction(ConsoleReportPrinter consoleReportPrinter,
                                   ConsoleReader consoleReader) {
        this.consoleReportPrinter = consoleReportPrinter;
        this.consoleReader = consoleReader;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    @Override
    public void execute() {
        log.info("User command started: SHOW_GUEST_SERVICES, sortBy={}", sortBy);

        try {
            System.out.print("Enter guest name: ");
            String guestName = consoleReader.nextLine();

            if ("price".equals(sortBy)) {
                consoleReportPrinter.printGuestServices(guestName, SortUtils.byServicePrice());
            } else {
                consoleReportPrinter.printGuestServices(guestName, SortUtils.byServiceDate());
            }

            log.info(
                    "User command completed successfully: SHOW_GUEST_SERVICES, guest={}, sortBy={}",
                    guestName, sortBy
            );
        } catch (GuestException e) {
            log.error("User command failed: SHOW_GUEST_SERVICES, sortBy={}", sortBy, e);
            System.out.println("Failed to show guest services: " + e.getMessage());
        }
    }
}
