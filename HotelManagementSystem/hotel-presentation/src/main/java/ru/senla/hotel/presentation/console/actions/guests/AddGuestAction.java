package ru.senla.hotel.presentation.console.actions.guests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.senla.hotel.service.GuestManager;
import ru.senla.hotel.service.exception.guest.GuestException;
import ru.senla.hotel.domain.model.Guest;
import ru.senla.hotel.presentation.console.actions.IAction;
import ru.senla.hotel.presentation.console.util.ConsoleReader;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AddGuestAction implements IAction {

    private static final Logger log = LoggerFactory.getLogger(AddGuestAction.class);

    private final GuestManager guestManager;
    private final ConsoleReader reader;

    @Autowired
    public AddGuestAction(GuestManager guestManager,
                          ConsoleReader reader) {
        this.guestManager = guestManager;
        this.reader = reader;
    }

    @Override
    public void execute() {
        log.info("User command started: ADD_GUEST");

        try {
            System.out.print("Enter guest name: ");
            String name = reader.nextLine();

            guestManager.addGuest(new Guest(null, name));

            log.info("User command completed successfully: ADD_GUEST, name={}", name);
            System.out.println("Guest successfully added.");
        } catch (GuestException e) {
            log.error("User command failed: ADD_GUEST", e);
            System.out.println("Failed to add guest: " + e.getMessage());
        }
    }
}
