package ru.senla.hotel.ui.actions.guests;

import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.exception.HotelException;
import ru.senla.hotel.management.Administrator;
import ru.senla.hotel.ui.menu.IAction;
import ru.senla.hotel.ui.util.ConsoleReader;

public class ShowGuestServicesAction implements IAction {

    @Inject
    private Administrator admin;

    @Inject
    private ConsoleReader reader;

    private final String sortBy;

    public ShowGuestServicesAction(String sortBy) {
        this.sortBy = sortBy;
    }

    @Override
    public void execute() {
        try {
            System.out.print("Enter guest name: ");
            String guestName = reader.nextLine();

            if ("price".equals(sortBy)) {
                admin.printGuestServicesByPrice(guestName);
            } else {
                admin.printGuestServicesByDate(guestName);
            }

        } catch (HotelException e) {
            System.out.println("Failed to show guest services: " + e.getMessage());
        }
    }
}
