package ru.senla.hotel.ui.actions.rooms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.di.annotation.Scope;
import ru.senla.hotel.service.exception.room.RoomException;
import ru.senla.hotel.service.Administrator;
import ru.senla.hotel.model.Room;
import ru.senla.hotel.ui.menu.IAction;
import ru.senla.hotel.ui.util.ConsoleReader;

@Component(scope = Scope.PROTOTYPE)
public class AddRoomAction implements IAction {

    @Inject
    private Administrator admin;

    @Inject
    private ConsoleReader reader;

    private static final Logger log = LoggerFactory.getLogger(AddRoomAction.class);

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
            double price = reader.nextDouble();

            admin.addRoom(new Room(null, number, capacity, stars, price));

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
