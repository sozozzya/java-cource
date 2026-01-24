package ru.senla.hotel.ui.actions.bookings;

import ru.senla.hotel.exception.booking.BookingException;
import ru.senla.hotel.exception.room.RoomException;
import ru.senla.hotel.management.Administrator;
import ru.senla.hotel.ui.menu.IAction;
import ru.senla.hotel.ui.util.ConsoleReader;

public class CheckOutAction implements IAction {
    private final Administrator admin;

    public CheckOutAction(Administrator admin) {
        this.admin = admin;
    }

    @Override
    public void execute() {
        try {
            ConsoleReader reader = ConsoleReader.getInstance();

            System.out.print("Enter room number to check out: ");
            int roomNumber = reader.nextInt();

            admin.checkOut(roomNumber);
            System.out.println("Guest successfully checked out.");

        } catch (BookingException | RoomException e) {
            System.out.println("Check-out failed: " + e.getMessage());
        }
    }
}
