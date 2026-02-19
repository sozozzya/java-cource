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
public class ExportGuestsAction implements IAction {

    private static final Logger log = LoggerFactory.getLogger(ExportGuestsAction.class);

    private final GuestManager guestManager;
    private final ConsoleReader reader;

    @Autowired
    public ExportGuestsAction(GuestManager guestManager,
                              ConsoleReader reader) {
        this.guestManager = guestManager;
        this.reader = reader;
    }

    @Override
    public void execute() {
        log.info("User command started: EXPORT_GUESTS");

        try {
            System.out.print("Enter CSV file path to export guests: ");
            String path = reader.nextLine();

            guestManager.exportGuestToCSV(path);

            log.info("User command completed successfully: EXPORT_GUESTS, path={}", path);
            System.out.println("Guests exported successfully.");
        } catch (GuestException e) {
            log.error("User command failed: EXPORT_GUESTS", e);
            System.out.println("Failed to export guests: " + e.getMessage());
        }
    }
}
