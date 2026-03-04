package ru.senla.hotel.ui.actions.rooms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.di.annotation.Scope;
import ru.senla.hotel.service.exception.room.RoomException;
import ru.senla.hotel.service.Administrator;
import ru.senla.hotel.ui.menu.IAction;
import ru.senla.hotel.ui.util.ConsoleReader;

@Component(scope = Scope.PROTOTYPE)
public class ImportRoomsAction implements IAction {

    @Inject
    private Administrator admin;

    @Inject
    private ConsoleReader reader;

    private static final Logger log = LoggerFactory.getLogger(ImportRoomsAction.class);

    @Override
    public void execute() {
        log.info("User command started: IMPORT_ROOMS");

        try {
            System.out.print("Enter CSV file path to import rooms: ");
            String path = reader.nextLine();

            admin.importRooms(path);

            log.info("User command completed successfully: IMPORT_ROOMS, path={}", path);
            System.out.println("Rooms imported successfully.");
        } catch (RoomException e) {
            log.error("User command failed: IMPORT_ROOMS", e);
            System.out.println("Failed to import rooms: " + e.getMessage());
        }
    }
}
