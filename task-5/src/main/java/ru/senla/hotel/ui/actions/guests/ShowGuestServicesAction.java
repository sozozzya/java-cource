package ru.senla.hotel.ui.actions.guests;

import ru.senla.hotel.management.Administrator;
import ru.senla.hotel.model.Guest;
import ru.senla.hotel.ui.menu.IAction;

public class ShowGuestServicesAction implements IAction {
    private final Administrator admin;
    private final String sortBy;

    public ShowGuestServicesAction(Administrator admin, String sortBy) {
        this.admin = admin;
        this.sortBy = sortBy;
    }

    @Override
    public void execute() {
        try {
            java.util.Scanner s = new java.util.Scanner(System.in);
            System.out.print("Enter guest name: ");
            String name = s.nextLine();
            Guest guest = new Guest(name);
            if ("price".equals(sortBy)) admin.printGuestServicesByPrice(guest);
            else admin.printGuestServicesByDate(guest);
        } catch (Exception e) {
            System.out.println("Failed to show guest services: " + e.getMessage());
        }
    }
}
