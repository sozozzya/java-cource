package ru.senla.hotel.ui.actions.guests;

import ru.senla.hotel.management.Administrator;
import ru.senla.hotel.model.Guest;
import ru.senla.hotel.ui.menu.IAction;
import ru.senla.hotel.ui.util.ConsoleReader;

public class ShowGuestBillAction implements IAction {
    private final Administrator admin;

    public ShowGuestBillAction(Administrator admin) {
        this.admin = admin;
    }

    @Override
    public void execute() {
        try {
            ConsoleReader reader = ConsoleReader.getInstance();
            System.out.print("Enter guest name: ");
            String name = reader.nextLine();
            Guest guest = new Guest(name);
            admin.printGuestBill(guest);
        } catch (Exception e) {
            System.out.println("Failed to show guest bill: " + e.getMessage());
        }
    }
}
