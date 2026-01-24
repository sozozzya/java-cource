package ru.senla.hotel.ui.actions.bookings;

import ru.senla.hotel.exception.booking.BookingException;
import ru.senla.hotel.management.Administrator;
import ru.senla.hotel.ui.menu.IAction;
import ru.senla.hotel.ui.util.ConsoleReader;

public class ImportBookingsAction implements IAction {

    private final Administrator admin;

    public ImportBookingsAction(Administrator admin) {
        this.admin = admin;
    }

    @Override
    public void execute() {
        try {
            ConsoleReader reader = ConsoleReader.getInstance();

            System.out.print("Enter CSV file path to import bookings: ");
            String path = reader.nextLine();

            admin.importBookings(path);
            System.out.println("Bookings imported successfully.");

        } catch (BookingException e) {
            System.out.println("Failed to import bookings: " + e.getMessage());
        }
    }
}
