package ru.senla.hotel.ui.actions.guests;

import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.di.annotation.Scope;
import ru.senla.hotel.exception.guest.GuestException;
import ru.senla.hotel.management.Administrator;
import ru.senla.hotel.ui.menu.IAction;
import ru.senla.hotel.ui.util.ConsoleReader;

@Component(scope = Scope.PROTOTYPE)
public class ExportGuestsAction implements IAction {

    @Inject
    private Administrator admin;

    @Inject
    private ConsoleReader reader;

    @Override
    public void execute() {
        try {
            System.out.print("Enter CSV file path to export guests: ");
            String path = reader.nextLine();

            admin.exportGuests(path);
            System.out.println("Guests exported successfully.");

        } catch (GuestException e) {
            System.out.println("Failed to export guests: " + e.getMessage());
        }
    }
}
