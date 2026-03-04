package ru.senla.hotel.ui.actions.guests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.di.annotation.Scope;
import ru.senla.hotel.service.exception.guest.GuestException;
import ru.senla.hotel.service.Administrator;
import ru.senla.hotel.ui.menu.IAction;
import ru.senla.hotel.ui.util.ConsoleReader;

@Component(scope = Scope.PROTOTYPE)
public class ExportGuestsAction implements IAction {

    @Inject
    private Administrator admin;

    @Inject
    private ConsoleReader reader;

    private static final Logger log = LoggerFactory.getLogger(ExportGuestsAction.class);

    @Override
    public void execute() {
        log.info("User command started: EXPORT_GUESTS");

        try {
            System.out.print("Enter CSV file path to export guests: ");
            String path = reader.nextLine();

            admin.exportGuests(path);

            log.info("User command completed successfully: EXPORT_GUESTS, path={}", path);
            System.out.println("Guests exported successfully.");
        } catch (GuestException e) {
            log.error("User command failed: EXPORT_GUESTS", e);
            System.out.println("Failed to export guests: " + e.getMessage());
        }
    }
}
