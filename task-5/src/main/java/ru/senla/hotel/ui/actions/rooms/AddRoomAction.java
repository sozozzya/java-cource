package ru.senla.hotel.ui.actions.rooms;

import ru.senla.hotel.management.Administrator;
import ru.senla.hotel.model.Room;
import ru.senla.hotel.ui.menu.IAction;
import ru.senla.hotel.ui.util.ConsoleReader;

public class AddRoomAction implements IAction {
    private final Administrator admin;

    public AddRoomAction(Administrator admin) {
        this.admin = admin;
    }

    @Override
    public void execute() {
        try {
            ConsoleReader reader = ConsoleReader.getInstance();
            System.out.print("Enter room number: ");
            int number = reader.nextInt();
            System.out.print("Enter capacity: ");
            int capacity = reader.nextInt();
            System.out.print("Enter stars: ");
            int stars = reader.nextInt();
            System.out.print("Enter price per night: ");
            double price = reader.nextDouble();

            Room room = new Room(number, capacity, stars, price);
            admin.addRoom(room);
        } catch (Exception e) {
            System.out.println("Failed to add room: " + e.getMessage());
        }
    }
}
