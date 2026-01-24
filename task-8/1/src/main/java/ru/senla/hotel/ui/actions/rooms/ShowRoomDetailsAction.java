package ru.senla.hotel.ui.actions.rooms;

import ru.senla.hotel.exception.HotelException;
import ru.senla.hotel.management.Administrator;
import ru.senla.hotel.ui.menu.IAction;
import ru.senla.hotel.ui.util.ConsoleReader;

public class ShowRoomDetailsAction implements IAction {
    private final Administrator admin;

    public ShowRoomDetailsAction(Administrator admin) {
        this.admin = admin;
    }

    @Override
    public void execute() {
        try {
            ConsoleReader reader = ConsoleReader.getInstance();
            System.out.print("Enter room number: ");
            int number = reader.nextInt();
            admin.printRoomDetails(number);
        } catch (HotelException e) {
            System.out.println("Failed to show room details: " + e.getMessage());
        }
    }
}
