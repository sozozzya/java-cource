package ru.senla.hotel.presentation.console.actions.rooms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.senla.hotel.service.RoomManager;
import ru.senla.hotel.service.exception.room.RoomException;
import ru.senla.hotel.domain.model.Room;
import ru.senla.hotel.presentation.console.actions.IAction;
import ru.senla.hotel.presentation.console.util.ConsoleReader;

import java.math.BigDecimal;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AddRoomAction implements IAction {

    private static final Logger log = LoggerFactory.getLogger(AddRoomAction.class);

    private final RoomManager roomManager;
    private final ConsoleReader reader;

    @Autowired
    public AddRoomAction(RoomManager roomManager,
                         ConsoleReader reader) {
        this.roomManager = roomManager;
        this.reader = reader;
    }

    @Override
    public void execute() {
        log.info("User command started: ADD_ROOM");

        try {
            System.out.print("Enter room number: ");
            int number = reader.nextInt();

            System.out.print("Enter capacity: ");
            int capacity = reader.nextInt();

            System.out.print("Enter stars: ");
            int stars = reader.nextInt();

            System.out.print("Enter price per night: ");
            BigDecimal price = reader.nextBigDecimal();

            roomManager.addRoom(new Room(null, number, capacity, stars, price));

            log.info("User command completed successfully: ADD_ROOM, number={}", number);
            System.out.println("Room successfully added.");
        } catch (RoomException e) {
            log.error("User command failed: ADD_ROOM", e);
            System.out.println("Failed to add room: " + e.getMessage());
        } catch (Exception e) {
            log.error("User command failed due to invalid input: ADD_ROOM", e);
            System.out.println("Invalid input.");
        }
    }
}
