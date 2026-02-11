package ru.senla.hotel.ui.actions.rooms;

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
        try {
            ConsoleReader reader = ConsoleReader.getInstance();
            System.out.print("Enter room number: ");
            int number = reader.nextInt();
            System.out.print("Set maintenance? (yes/no): ");
            String yn = reader.nextLine().trim().toLowerCase();
            boolean status = yn.equals("yes") || yn.equals("y");
            admin.setRoomMaintenance(number, status);
        } catch (Exception e) {
            System.out.println("Failed to change maintenance status: " + e.getMessage());
        }
    }
}
