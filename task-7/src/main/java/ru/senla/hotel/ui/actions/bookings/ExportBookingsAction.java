package ru.senla.hotel.ui.actions.bookings;

import ru.senla.hotel.exception.booking.BookingException;
import ru.senla.hotel.management.Administrator;
import ru.senla.hotel.ui.menu.IAction;
import ru.senla.hotel.ui.util.ConsoleReader;

public class ExportBookingsAction implements IAction {

    private final Administrator admin;

    public ExportBookingsAction(Administrator admin) {
        this.admin = admin;
    }

    @Override
    public void execute() {
        try {
            ConsoleReader reader = ConsoleReader.getInstance();

            System.out.print("Enter CSV file path to export bookings: ");
            String path = reader.nextLine();

            admin.exportBookings(path);
            System.out.println("Bookings exported successfully.");

        } catch (BookingException e) {
            System.out.println("Failed to export bookings: " + e.getMessage());
        }
    }
}
