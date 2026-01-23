package ru.senla.hotel.ui.actions.guests;

import ru.senla.hotel.management.Administrator;
import ru.senla.hotel.model.Guest;
import ru.senla.hotel.ui.menu.IAction;
import ru.senla.hotel.ui.util.ConsoleReader;

import java.time.LocalDate;

public class AssignServiceToGuestAction implements IAction {
    private final Administrator admin;

    public AssignServiceToGuestAction(Administrator admin) {
        this.admin = admin;
    }

    @Override
    public void execute() {
        try {
            ConsoleReader reader = ConsoleReader.getInstance();
            System.out.print("Enter guest name: ");
            String name = reader.nextLine();
            System.out.print("Enter service name: ");
            String serviceName = reader.nextLine();
            System.out.print("Enter service date (YYYY-MM-DD): ");
            LocalDate date = LocalDate.parse(reader.nextLine());
            Guest guest = new Guest(name);
            admin.assignServiceToGuest(guest, serviceName, date);
        } catch (Exception e) {
            System.out.println("Failed to assign service: " + e.getMessage());
        }
    }
}
