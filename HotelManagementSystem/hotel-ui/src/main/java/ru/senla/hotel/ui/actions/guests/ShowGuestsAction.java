package ru.senla.hotel.ui.actions.guests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.service.Administrator;
import ru.senla.hotel.service.exception.guest.GuestException;
import ru.senla.hotel.ui.menu.IAction;

public class ShowGuestsAction implements IAction {

    @Inject
    private Administrator admin;

    private final String mode;

    public ShowGuestsAction(String mode) {
        this.mode = mode;
    }

    private static final Logger log = LoggerFactory.getLogger(ShowGuestsAction.class);

    @Override
    public void execute() {
        log.info("User command started: SHOW_GUESTS, mode={}", mode);

        try {
            if ("name".equals(mode)) {
                admin.printGuestsByName();
            } else {
                admin.printGuestsByCheckoutDate();
            }

            log.info("User command completed successfully: SHOW_GUESTS, mode={}", mode);
        } catch (GuestException e) {
            log.error("User command failed: SHOW_GUESTS, mode={}", mode, e);
            System.out.println("Failed to show guests: " + e.getMessage());
        }
    }
}

