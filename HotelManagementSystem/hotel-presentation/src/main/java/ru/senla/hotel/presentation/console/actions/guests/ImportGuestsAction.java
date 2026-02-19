package ru.senla.hotel.presentation.console.actions.guests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.senla.hotel.service.GuestManager;
import ru.senla.hotel.service.exception.guest.GuestException;
import ru.senla.hotel.presentation.console.actions.IAction;
import ru.senla.hotel.presentation.console.util.ConsoleReader;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ImportGuestsAction implements IAction {

    private static final Logger log = LoggerFactory.getLogger(ImportGuestsAction.class);

    private final GuestManager guestManager;
    private final ConsoleReader reader;

    @Autowired
    public ImportGuestsAction(GuestManager guestManager,
                              ConsoleReader reader) {
        this.guestManager = guestManager;
        this.reader = reader;
    }

    @Override
    public void execute() {
        log.info("User command started: IMPORT_GUESTS");

        try {
            System.out.print("Enter CSV file path to import guests: ");
            String path = reader.nextLine();

            guestManager.importGuestFromCSV(path);

            log.info("User command completed successfully: IMPORT_GUESTS, path={}", path);
            System.out.println("Guests imported successfully.");
        } catch (GuestException e) {
            log.error("User command failed: IMPORT_GUESTS", e);
            System.out.println("Failed to import guests: " + e.getMessage());
        }
    }
}
