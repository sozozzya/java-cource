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
public class ExportRoomsAction implements IAction {

    private static final Logger log = LoggerFactory.getLogger(ExportRoomsAction.class);

    private final RoomManager roomManager;
    private final ConsoleReader reader;

    @Autowired
    public ExportRoomsAction(RoomManager roomManager,
                             ConsoleReader reader) {
        this.roomManager = roomManager;
        this.reader = reader;
    }

    @Override
    public void execute() {
        log.info("User command started: EXPORT_ROOMS");

        try {
            System.out.print("Enter CSV file path to export rooms: ");
            String path = reader.nextLine();

            roomManager.exportRoomToCSV(path);

            log.info("User command completed successfully: EXPORT_ROOMS, path={}", path);
            System.out.println("Rooms exported successfully.");
        } catch (RoomException e) {
            log.error("User command failed: EXPORT_ROOMS", e);
            System.out.println("Failed to export rooms: " + e.getMessage());
        }
    }
}
