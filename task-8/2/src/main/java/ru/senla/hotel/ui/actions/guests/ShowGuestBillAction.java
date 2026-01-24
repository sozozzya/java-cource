package ru.senla.hotel.ui.actions.guests;

import ru.senla.hotel.exception.HotelException;
import ru.senla.hotel.management.Administrator;
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
            String guestName = reader.nextLine();
            admin.printGuestBill(guestName);
        } catch (HotelException e) {
            System.out.println("Failed to show guest bill: " + e.getMessage());
        }
    }
}
