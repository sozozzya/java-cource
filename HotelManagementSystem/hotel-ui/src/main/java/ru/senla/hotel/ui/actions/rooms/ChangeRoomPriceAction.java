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
public class ChangeRoomPriceAction implements IAction {

    @Inject
    private Administrator admin;

    @Inject
    private ConsoleReader reader;

    private static final Logger log = LoggerFactory.getLogger(ChangeRoomPriceAction.class);

    @Override
    public void execute() {
        log.info("User command started: CHANGE_ROOM_PRICE");

        try {
            System.out.print("Enter room number: ");
            int number = reader.nextInt();

            System.out.print("Enter new price: ");
            double price = reader.nextDouble();

            admin.changeRoomPrice(number, price);

            log.info("User command completed successfully: CHANGE_ROOM_PRICE, roomNumber={}", number);
            System.out.println("Room price updated successfully.");
        } catch (RoomException e) {
            log.error("User command failed: CHANGE_ROOM_PRICE", e);
            System.out.println("Failed to change room price: " + e.getMessage());
        } catch (Exception e) {
            log.error("User command failed due to invalid input: CHANGE_ROOM_PRICE", e);
            System.out.println("Invalid input.");
        }
    }
}
