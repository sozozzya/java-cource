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
public class SetMaintenanceAction implements IAction {

    private static final Logger log = LoggerFactory.getLogger(SetMaintenanceAction.class);

    private final RoomManager roomManager;
    private final ConsoleReader reader;

    @Autowired
    public SetMaintenanceAction(RoomManager roomManager,
                                ConsoleReader reader) {
        this.roomManager = roomManager;
        this.reader = reader;
    }

    @Override
    public void execute() {
        log.info("User command started: SET_ROOM_MAINTENANCE");

        if (!roomManager.isRoomStatusChangeEnabled()) {
            log.error("User command rejected: SET_ROOM_MAINTENANCE – feature disabled");
            System.out.println(
                    "Operation is not allowed: changing room maintenance status is disabled by configuration."
            );
            return;
        }

        try {
            System.out.print("Enter room number: ");
            int number = reader.nextInt();

            System.out.print("Set maintenance? (yes/no): ");
            boolean status = reader.nextYesNo();

            roomManager.changeMaintenanceStatus(number, status);

            log.info(
                    "User command completed successfully: SET_ROOM_MAINTENANCE, roomNumber={}, status={}",
                    number, status
            );
            System.out.println("Maintenance status updated.");
        } catch (RoomException e) {
            log.error("User command failed: SET_ROOM_MAINTENANCE", e);
            System.out.println("Failed to change maintenance status: " + e.getMessage());
        }
    }
}
