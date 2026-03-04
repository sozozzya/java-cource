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
import ru.senla.hotel.service.util.SortUtils;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ShowAllRoomsAction implements IAction {

    private static final Logger log = LoggerFactory.getLogger(ShowAllRoomsAction.class);

    private final ConsoleReportPrinter consoleReportPrinter;
    private String sortBy;

    @Autowired
    public ShowAllRoomsAction(ConsoleReportPrinter consoleReportPrinter) {
        this.consoleReportPrinter = consoleReportPrinter;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    @Override
    public void execute() {
        log.info("User command started: SHOW_ALL_ROOMS, sortBy={}", sortBy);

        try {
            switch (sortBy) {
                case "capacity" -> consoleReportPrinter.printAllRooms(SortUtils.byRoomCapacity());
                case "stars" -> consoleReportPrinter.printAllRooms(SortUtils.byRoomStars());
                default -> consoleReportPrinter.printAllRooms(SortUtils.byRoomPrice());
            }

            log.info("User command completed successfully: SHOW_ALL_ROOMS, sortBy={}", sortBy);
        } catch (RoomException e) {
            log.error("User command failed: SHOW_ALL_ROOMS, sortBy={}", sortBy, e);
            System.out.println("Failed to show rooms: " + e.getMessage());
        }
    }
}
