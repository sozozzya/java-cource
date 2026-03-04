package ru.senla.hotel.ui.actions.guests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.di.annotation.Scope;
import ru.senla.hotel.service.exception.guest.GuestException;
import ru.senla.hotel.service.Administrator;
import ru.senla.hotel.model.Guest;
import ru.senla.hotel.ui.menu.IAction;
import ru.senla.hotel.ui.util.ConsoleReader;

@Component(scope = Scope.PROTOTYPE)
public class AddGuestAction implements IAction {

    @Inject
    private Administrator admin;

    @Inject
    private ConsoleReader reader;

    private static final Logger log = LoggerFactory.getLogger(AddGuestAction.class);

    @Override
    public void execute() {
        log.info("User command started: ADD_GUEST");

        try {
            System.out.print("Enter guest name: ");
            String name = reader.nextLine();

            admin.addGuest(new Guest(null, name));

            log.info("User command completed successfully: ADD_GUEST, name={}", name);
            System.out.println("Guest successfully added.");
        } catch (GuestException e) {
            log.error("User command failed: ADD_GUEST", e);
            System.out.println("Failed to add guest: " + e.getMessage());
        }
    }
}
