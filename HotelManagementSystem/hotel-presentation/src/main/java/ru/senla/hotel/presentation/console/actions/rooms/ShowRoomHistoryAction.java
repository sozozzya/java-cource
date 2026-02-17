package ru.senla.hotel.presentation.console.actions.rooms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.senla.hotel.presentation.console.ConsoleReportPrinter;
import ru.senla.hotel.service.exception.room.RoomException;
import ru.senla.hotel.presentation.console.actions.IAction;
import ru.senla.hotel.presentation.console.util.ConsoleReader;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ShowRoomHistoryAction implements IAction {

    private static final Logger log = LoggerFactory.getLogger(ShowRoomHistoryAction.class);

    private final ConsoleReportPrinter consoleReportPrinter;
    private final ConsoleReader reader;

    @Autowired
    public ShowRoomHistoryAction(ConsoleReportPrinter consoleReportPrinter,
                                 ConsoleReader reader) {
        this.consoleReportPrinter = consoleReportPrinter;
        this.reader = reader;
    }

    @Override
    public void execute() {
        log.info("User command started: SHOW_ROOM_HISTORY");

        try {
            System.out.print("Enter room number: ");
            int roomNumber = reader.nextInt();

            consoleReportPrinter.printRoomHistory(roomNumber);

            log.info("User command completed successfully: SHOW_ROOM_HISTORY, roomNumber={}", roomNumber);
        } catch (RoomException e) {
            log.error("User command failed: SHOW_ROOM_HISTORY", e);
            System.out.println("Failed to show room history: " + e.getMessage());
        }
    }
}
