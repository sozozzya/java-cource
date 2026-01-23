package ru.senla.hotel.ui.actions.rooms;

import ru.senla.hotel.exception.room.RoomException;
import ru.senla.hotel.management.Administrator;
import ru.senla.hotel.ui.menu.IAction;
import ru.senla.hotel.ui.util.ConsoleReader;

public class SetMaintenanceAction implements IAction {
    private final Administrator admin;

    public SetMaintenanceAction(Administrator admin) {
        this.admin = admin;
    }

    @Override
    public void execute() {
        if (!admin.isRoomStatusChangeEnabled()) {
            System.out.println(
                    "Operation is not allowed: changing room maintenance status is disabled by configuration."
            );
            return;
        }

        try {
            ConsoleReader reader = ConsoleReader.getInstance();

            System.out.print("Enter room number: ");
            int number = Integer.parseInt(reader.nextLine());

            System.out.print("Set maintenance? (yes/no): ");
            String yn = reader.nextLine().trim().toLowerCase();
            boolean status = yn.equals("yes") || yn.equals("y");

            admin.setRoomMaintenance(number, status);
            System.out.println("Maintenance status updated.");

        } catch (RoomException e) {
            System.out.println("Failed to change maintenance status: " + e.getMessage());
        }
    }
}
