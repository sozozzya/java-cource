package ru.senla.hotel.ui.actions.bookings;

import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.di.annotation.Scope;
import ru.senla.hotel.exception.booking.BookingException;
import ru.senla.hotel.exception.room.RoomException;
import ru.senla.hotel.management.Administrator;
import ru.senla.hotel.ui.menu.IAction;
import ru.senla.hotel.ui.util.ConsoleReader;

@Component(scope = Scope.PROTOTYPE)
public class CheckOutAction implements IAction {

    @Inject
    private Administrator admin;

    @Inject
    private ConsoleReader reader;

    @Override
    public void execute() {
        try {
            System.out.print("Enter room number to check out: ");
            int roomNumber = reader.nextInt();

            admin.checkOut(roomNumber);
            System.out.println("Guest successfully checked out.");

        } catch (BookingException | RoomException e) {
            System.out.println("Check-out failed: " + e.getMessage());
        }
    }
}
