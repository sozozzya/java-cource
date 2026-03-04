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

import java.math.BigDecimal;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ChangeRoomPriceAction implements IAction {

    private static final Logger log = LoggerFactory.getLogger(ChangeRoomPriceAction.class);

    private final RoomManager roomManager;
    private final ConsoleReader reader;

    @Autowired
    public ChangeRoomPriceAction(RoomManager roomManager,
                                 ConsoleReader reader) {
        this.roomManager = roomManager;
        this.reader = reader;
    }

    @Override
    public void execute() {
        log.info("User command started: CHANGE_ROOM_PRICE");

        try {
            System.out.print("Enter room number: ");
            int number = reader.nextInt();

            System.out.print("Enter new price: ");
            BigDecimal price = reader.nextBigDecimal();

            roomManager.changeRoomPrice(number, price);

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
