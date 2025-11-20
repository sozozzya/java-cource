package ru.senla.hotel.ui.actions.bookings;

import ru.senla.hotel.management.Administrator;
import ru.senla.hotel.ui.menu.IAction;
import ru.senla.hotel.ui.util.ConsoleReader;

import java.time.LocalDate;

public class ShowRoomsFreeByDateAction implements IAction {
    private final Administrator admin;

    public ShowRoomsFreeByDateAction(Administrator admin) {
        this.admin = admin;
    }

    @Override
    public void execute() {
        try {
            ConsoleReader r = ConsoleReader.getInstance();
            System.out.print("Enter date (YYYY-MM-DD): ");
            LocalDate date = LocalDate.parse(r.nextLine());
            admin.printRoomsFreeByDate(date);
        } catch (Exception e) {
            System.out.println("Failed to get rooms free by date: " + e.getMessage());
        }
    }
}
