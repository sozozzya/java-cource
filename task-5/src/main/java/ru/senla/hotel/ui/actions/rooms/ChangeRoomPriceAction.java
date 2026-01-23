package ru.senla.hotel.ui.actions.rooms;

import ru.senla.hotel.management.Administrator;
import ru.senla.hotel.ui.menu.IAction;
import ru.senla.hotel.ui.util.ConsoleReader;

public class ChangeRoomPriceAction implements IAction {
    private final Administrator admin;

    public ChangeRoomPriceAction(Administrator admin) {
        this.admin = admin;
    }

    @Override
    public void execute() {
        try {
            ConsoleReader reader = ConsoleReader.getInstance();
            System.out.print("Enter room number: ");
            int number = reader.nextInt();
            System.out.print("Enter new price: ");
            double price = reader.nextDouble();
            admin.changeRoomPrice(number, price);
        } catch (Exception e) {
            System.out.println("Failed to change room price: " + e.getMessage());
        }
    }
}
