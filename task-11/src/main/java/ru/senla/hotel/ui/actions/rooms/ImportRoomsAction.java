package ru.senla.hotel.ui.actions.rooms;

import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.di.annotation.Scope;
import ru.senla.hotel.exception.room.RoomException;
import ru.senla.hotel.management.Administrator;
import ru.senla.hotel.ui.menu.IAction;
import ru.senla.hotel.ui.util.ConsoleReader;

@Component(scope = Scope.PROTOTYPE)
public class ImportRoomsAction implements IAction {

    @Inject
    private Administrator admin;

    @Inject
    private ConsoleReader reader;

    @Override
    public void execute() {
        try {
            System.out.print("Enter CSV file path to import rooms: ");
            String path = reader.nextLine();

            admin.importRooms(path);
            System.out.println("Rooms imported successfully.");

        } catch (RoomException e) {
            System.out.println("Failed to import rooms: " + e.getMessage());
        }
    }
}
