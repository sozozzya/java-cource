package ru.senla.hotel.ui.actions.bookings;

import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.di.annotation.Scope;
import ru.senla.hotel.exception.booking.BookingException;
import ru.senla.hotel.management.Administrator;
import ru.senla.hotel.ui.menu.IAction;
import ru.senla.hotel.ui.util.ConsoleReader;

@Component(scope = Scope.PROTOTYPE)
public class ImportBookingsAction implements IAction {

    @Inject
    private Administrator admin;

    @Inject
    private ConsoleReader reader;

    @Override
    public void execute() {
        try {
            System.out.print("Enter CSV file path to import bookings: ");
            String path = reader.nextLine();

            admin.importBookings(path);
            System.out.println("Bookings imported successfully.");

        } catch (BookingException e) {
            System.out.println("Failed to import bookings: " + e.getMessage());
        }
    }
}
