package ru.senla.hotel.ui.actions.rooms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.di.annotation.Scope;
import ru.senla.hotel.service.Administrator;
import ru.senla.hotel.service.exception.room.RoomException;
import ru.senla.hotel.ui.menu.IAction;
import ru.senla.hotel.ui.util.ConsoleReader;

@Component(scope = Scope.PROTOTYPE)
public class ShowRoomHistoryAction implements IAction {

    @Inject
    private Administrator admin;

    @Inject
    private ConsoleReader reader;

    private static final Logger log = LoggerFactory.getLogger(ShowRoomHistoryAction.class);

    @Override
    public void execute() {
        log.info("User command started: SHOW_ROOM_HISTORY");

        try {
            System.out.print("Enter room number: ");
            int roomNumber = reader.nextInt();

            admin.printRoomHistory(roomNumber);

            log.info("User command completed successfully: SHOW_ROOM_HISTORY, roomNumber={}", roomNumber);
        } catch (RoomException e) {
            log.error("User command failed: SHOW_ROOM_HISTORY", e);
            System.out.println("Failed to show room history: " + e.getMessage());
        }
    }
}
