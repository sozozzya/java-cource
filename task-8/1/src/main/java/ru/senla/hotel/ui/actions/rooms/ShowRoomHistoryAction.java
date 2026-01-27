package ru.senla.hotel.ui.actions.rooms;

import ru.senla.hotel.exception.HotelException;
import ru.senla.hotel.management.Administrator;
import ru.senla.hotel.ui.menu.IAction;
import ru.senla.hotel.ui.util.ConsoleReader;

public class ShowRoomHistoryAction implements IAction {
    private final Administrator admin;

    public ShowRoomHistoryAction(Administrator admin) {
        this.admin = admin;
    }

    @Override
    public void execute() {
        try {
            ConsoleReader r = ConsoleReader.getInstance();
            System.out.print("Enter room number: ");
            int roomNumber = r.nextInt();
            admin.printRoomHistory(roomNumber);
        } catch (HotelException e) {
            System.out.println("Failed to show room history: " + e.getMessage());
        }
    }
}
