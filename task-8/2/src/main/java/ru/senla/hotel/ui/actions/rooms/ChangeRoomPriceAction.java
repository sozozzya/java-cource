package ru.senla.hotel.ui.actions.rooms;

import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.di.annotation.Scope;
import ru.senla.hotel.exception.room.RoomException;
import ru.senla.hotel.management.Administrator;
import ru.senla.hotel.ui.menu.IAction;
import ru.senla.hotel.ui.util.ConsoleReader;

@Component(scope = Scope.PROTOTYPE)
public class ChangeRoomPriceAction implements IAction {

    @Inject
    private Administrator admin;

    @Inject
    private ConsoleReader reader;

    @Override
    public void execute() {
        try {
            System.out.print("Enter room number: ");
            int number = reader.nextInt();

            System.out.print("Enter new price: ");
            double price = reader.nextDouble();

            admin.changeRoomPrice(number, price);
            System.out.println("Room price updated successfully.");

        } catch (RoomException e) {
            System.out.println("Failed to change room price: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Invalid input.");
        }
    }
}
