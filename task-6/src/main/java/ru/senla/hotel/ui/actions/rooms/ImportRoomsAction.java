package ru.senla.hotel.ui.actions.rooms;

import ru.senla.hotel.exception.room.RoomException;
import ru.senla.hotel.management.Administrator;
import ru.senla.hotel.ui.menu.IAction;
import ru.senla.hotel.ui.util.ConsoleReader;

public class ImportRoomsAction implements IAction {

    private final Administrator admin;

    public ImportRoomsAction(Administrator admin) {
        this.admin = admin;
    }

    @Override
    public void execute() {
        try {
            ConsoleReader reader = ConsoleReader.getInstance();

            System.out.print("Enter CSV file path to import rooms: ");
            String path = reader.nextLine();

            admin.importRooms(path);
            System.out.println("Rooms imported successfully.");

        } catch (RoomException e) {
            System.out.println("Failed to import rooms: " + e.getMessage());
        }
    }
}
