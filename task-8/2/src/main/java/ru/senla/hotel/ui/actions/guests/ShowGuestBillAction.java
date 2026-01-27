package ru.senla.hotel.ui.actions.guests;

import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.di.annotation.Scope;
import ru.senla.hotel.exception.HotelException;
import ru.senla.hotel.management.Administrator;
import ru.senla.hotel.ui.menu.IAction;
import ru.senla.hotel.ui.util.ConsoleReader;

@Component(scope = Scope.PROTOTYPE)
public class ShowGuestBillAction implements IAction {

    @Inject
    private Administrator admin;

    @Inject
    private ConsoleReader reader;

    @Override
    public void execute() {
        try {
            System.out.print("Enter guest name: ");
            String guestName = reader.nextLine();

            admin.printGuestBill(guestName);

        } catch (HotelException e) {
            System.out.println("Failed to show guest bill: " + e.getMessage());
        }
    }
}
