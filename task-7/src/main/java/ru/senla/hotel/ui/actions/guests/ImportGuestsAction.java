package ru.senla.hotel.ui.actions.guests;

import ru.senla.hotel.exception.guest.GuestException;
import ru.senla.hotel.management.Administrator;
import ru.senla.hotel.ui.menu.IAction;
import ru.senla.hotel.ui.util.ConsoleReader;

public class ImportGuestsAction implements IAction {

    private final Administrator admin;

    public ImportGuestsAction(Administrator admin) {
        this.admin = admin;
    }

    @Override
    public void execute() {
        try {
            ConsoleReader reader = ConsoleReader.getInstance();

            System.out.print("Enter CSV file path to import guests: ");
            String path = reader.nextLine();

            admin.importGuests(path);
            System.out.println("Guests imported successfully.");

        } catch (GuestException e) {
            System.out.println("Failed to import guests: " + e.getMessage());
        }
    }
}
