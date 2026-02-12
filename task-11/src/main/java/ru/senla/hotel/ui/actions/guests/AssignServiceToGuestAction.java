package ru.senla.hotel.ui.actions.guests;

import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.di.annotation.Scope;
import ru.senla.hotel.exception.guest.GuestException;
import ru.senla.hotel.exception.service.ServiceException;
import ru.senla.hotel.management.Administrator;
import ru.senla.hotel.ui.menu.IAction;
import ru.senla.hotel.ui.util.ConsoleReader;

import java.time.LocalDate;

@Component(scope = Scope.PROTOTYPE)
public class AssignServiceToGuestAction implements IAction {

    @Inject
    private Administrator admin;

    @Inject
    private ConsoleReader reader;

    @Override
    public void execute() {
        try {
            System.out.print("Enter guest name: ");
            String guestName = reader.nextLine();

            System.out.print("Enter service name: ");
            String serviceName = reader.nextLine();

            System.out.print("Enter service date (YYYY-MM-DD): ");
            LocalDate date = LocalDate.parse(reader.nextLine());

            admin.assignServiceToGuest(guestName, serviceName, date);
            System.out.println("Service successfully assigned to guest.");

        } catch (GuestException | ServiceException e) {
            System.out.println("Failed to assign service: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Invalid input.");
        }
    }
}
