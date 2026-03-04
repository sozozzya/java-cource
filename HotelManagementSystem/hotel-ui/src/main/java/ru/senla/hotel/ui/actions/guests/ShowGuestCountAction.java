package ru.senla.hotel.ui.actions.guests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.di.annotation.Scope;
import ru.senla.hotel.service.Administrator;
import ru.senla.hotel.service.exception.guest.GuestException;
import ru.senla.hotel.ui.menu.IAction;

@Component(scope = Scope.PROTOTYPE)
public class ShowGuestCountAction implements IAction {

    @Inject
    private Administrator admin;

    private static final Logger log = LoggerFactory.getLogger(ShowGuestCountAction.class);

    @Override
    public void execute() {
        log.info("User command started: SHOW_GUEST_COUNT");

        try {
            admin.printGuestCount();

            log.info("User command completed successfully: SHOW_GUEST_COUNT");
        } catch (GuestException e) {
            log.error("User command failed: SHOW_GUEST_COUNT", e);
            System.out.println("Failed to show guest count: " + e.getMessage());
        }
    }
}
