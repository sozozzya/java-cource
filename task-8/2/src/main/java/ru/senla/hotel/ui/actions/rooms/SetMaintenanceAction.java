package ru.senla.hotel.ui.actions.rooms;

import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.di.annotation.Scope;
import ru.senla.hotel.exception.room.RoomException;
import ru.senla.hotel.management.Administrator;
import ru.senla.hotel.ui.menu.IAction;
import ru.senla.hotel.ui.util.ConsoleReader;

@Component(scope = Scope.PROTOTYPE)
public class SetMaintenanceAction implements IAction {

    @Inject
    private Administrator admin;

    @Inject
    private ConsoleReader reader;

    @Override
    public void execute() {
        if (!admin.isRoomStatusChangeEnabled()) {
            System.out.println("Operation is not allowed: changing room maintenance status is disabled by configuration.");
            return;
        }

        try {
            System.out.print("Enter room number: ");
            int number = reader.nextInt();

            System.out.print("Set maintenance? (yes/no): ");
            boolean status = reader.nextYesNo();

            admin.setRoomMaintenance(number, status);
            System.out.println("Maintenance status updated.");

        } catch (RoomException e) {
            System.out.println("Failed to change maintenance status: " + e.getMessage());
        }
    }
}
