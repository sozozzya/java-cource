package ru.senla.hotel.ui.actions.guests;

import ru.senla.hotel.exception.guest.GuestException;
import ru.senla.hotel.management.Administrator;
import ru.senla.hotel.ui.menu.IAction;
import ru.senla.hotel.ui.util.ConsoleReader;

public class ExportGuestsAction implements IAction {

    private final Administrator admin;

    public ExportGuestsAction(Administrator admin) {
        this.admin = admin;
    }

    @Override
    public void execute() {
        try {
            ConsoleReader reader = ConsoleReader.getInstance();

            System.out.print("Enter CSV file path to export guests: ");
            String path = reader.nextLine();

            admin.exportGuests(path);
            System.out.println("Guests exported successfully.");

        } catch (GuestException e) {
            System.out.println("Failed to export guests: " + e.getMessage());
        }
    }
}
