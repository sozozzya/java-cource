package ru.senla.hotel.ui.actions.guests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.service.Administrator;
import ru.senla.hotel.service.exception.guest.GuestException;
import ru.senla.hotel.ui.menu.IAction;
import ru.senla.hotel.ui.util.ConsoleReader;

public class ShowGuestServicesAction implements IAction {

    @Inject
    private Administrator admin;

    @Inject
    private ConsoleReader reader;

    private final String sortBy;

    public ShowGuestServicesAction(String sortBy) {
        this.sortBy = sortBy;
    }

    private static final Logger log = LoggerFactory.getLogger(ShowGuestServicesAction.class);

    @Override
    public void execute() {
        log.info("User command started: SHOW_GUEST_SERVICES, sortBy={}", sortBy);

        try {
            System.out.print("Enter guest name: ");
            String guestName = reader.nextLine();

            if ("price".equals(sortBy)) {
                admin.printGuestServicesByPrice(guestName);
            } else {
                admin.printGuestServicesByDate(guestName);
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
