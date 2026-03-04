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
public class SetMaintenanceAction implements IAction {

    @Inject
    private Administrator admin;

    @Inject
    private ConsoleReader reader;

    private static final Logger log = LoggerFactory.getLogger(SetMaintenanceAction.class);

    @Override
    public void execute() {
        log.info("User command started: SET_ROOM_MAINTENANCE");

        if (!admin.isRoomStatusChangeEnabled()) {
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

            admin.setRoomMaintenance(number, status);

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
