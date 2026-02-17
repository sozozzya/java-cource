package ru.senla.hotel.presentation.console.actions.rooms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.senla.hotel.service.RoomManager;
import ru.senla.hotel.service.exception.room.RoomException;
import ru.senla.hotel.presentation.console.actions.IAction;
import ru.senla.hotel.presentation.console.util.ConsoleReader;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ImportRoomsAction implements IAction {

    private static final Logger log = LoggerFactory.getLogger(ImportRoomsAction.class);

    private final RoomManager roomManager;
    private final ConsoleReader reader;

    @Autowired
    public ImportRoomsAction(RoomManager roomManager,
                             ConsoleReader reader) {
        this.roomManager = roomManager;
        this.reader = reader;
    }

    @Override
    public void execute() {
        log.info("User command started: IMPORT_ROOMS");

        try {
            System.out.print("Enter CSV file path to import rooms: ");
            String path = reader.nextLine();

            roomManager.importRoomFromCSV(path);

            log.info("User command completed successfully: IMPORT_ROOMS, path={}", path);
            System.out.println("Rooms imported successfully.");
        } catch (RoomException e) {
            log.error("User command failed: IMPORT_ROOMS", e);
            System.out.println("Failed to import rooms: " + e.getMessage());
        }
    }
}
