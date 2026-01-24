package ru.senla.hotel.ui.actions.guests;

import ru.senla.hotel.exception.guest.GuestException;
import ru.senla.hotel.management.Administrator;
import ru.senla.hotel.model.Guest;
import ru.senla.hotel.ui.menu.IAction;
import ru.senla.hotel.ui.util.ConsoleReader;

public class AddGuestAction implements IAction {

    private final Administrator admin;

    public AddGuestAction(Administrator admin) {
        this.admin = admin;
    }

    @Override
    public void execute() {
        try {
            ConsoleReader reader = ConsoleReader.getInstance();

            System.out.print("Enter guest name: ");
            String name = reader.nextLine();
            Guest guest = new Guest(null, name);
            admin.addGuest(guest);

            System.out.println("Guest successfully added.");

        } catch (GuestException e) {
            System.out.println("Failed to add guest: " + e.getMessage());
        }
    }
}
