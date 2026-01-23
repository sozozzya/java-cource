package ru.senla.hotel.ui.actions.bookings;

import ru.senla.hotel.exception.booking.BookingException;
import ru.senla.hotel.exception.guest.GuestException;
import ru.senla.hotel.exception.room.RoomException;
import ru.senla.hotel.management.Administrator;
import ru.senla.hotel.ui.menu.IAction;
import ru.senla.hotel.ui.util.ConsoleReader;

import java.time.LocalDate;

public class CheckInAction implements IAction {
    private final Administrator admin;

    public CheckInAction(Administrator admin) {
        this.admin = admin;
    }

    @Override
    public void execute() {
        try {
            ConsoleReader reader = ConsoleReader.getInstance();

            System.out.print("Enter guest name: ");
            String guestName = reader.nextLine();
            System.out.print("Enter room number: ");
            int roomNumber = reader.nextInt();
            System.out.print("Enter check-in date (YYYY-MM-DD): ");
            LocalDate checkIn = LocalDate.parse(reader.nextLine());
            System.out.print("Enter check-out date (YYYY-MM-DD): ");
            LocalDate checkOut = LocalDate.parse(reader.nextLine());

            admin.checkIn(guestName, roomNumber, checkIn, checkOut);
            System.out.println("Guest successfully checked in.");

        } catch (BookingException | RoomException | GuestException e) {
            System.out.println("Check-in failed: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Invalid input.");
        }
    }
}
