package ru.senla.hotel.ui.actions.rooms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.service.Administrator;
import ru.senla.hotel.service.exception.room.RoomException;
import ru.senla.hotel.ui.menu.IAction;

public class ShowAllRoomsAction implements IAction {

    @Inject
    private Administrator admin;

    private final String sortBy;

    public ShowAllRoomsAction(String sortBy) {
        this.sortBy = sortBy;
    }

    private static final Logger log = LoggerFactory.getLogger(ShowAllRoomsAction.class);

    @Override
    public void execute() {
        log.info("User command started: SHOW_ALL_ROOMS, sortBy={}", sortBy);

        try {
            switch (sortBy) {
                case "capacity" -> admin.printAllRoomsByCapacity();
                case "stars" -> admin.printAllRoomsByStars();
                default -> admin.printAllRoomsByPrice();
            }

            log.info("User command completed successfully: SHOW_ALL_ROOMS, sortBy={}", sortBy);
        } catch (RoomException e) {
            log.error("User command failed: SHOW_ALL_ROOMS, sortBy={}", sortBy, e);
            System.out.println("Failed to show rooms: " + e.getMessage());
        }
    }
}
