package ru.senla.hotel.ui.actions.rooms;

import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.di.annotation.Scope;
import ru.senla.hotel.exception.room.RoomException;
import ru.senla.hotel.management.Administrator;
import ru.senla.hotel.model.Room;
import ru.senla.hotel.ui.menu.IAction;
import ru.senla.hotel.ui.util.ConsoleReader;

@Component(scope = Scope.PROTOTYPE)
public class AddRoomAction implements IAction {

    @Inject
    private Administrator admin;

    @Inject
    private ConsoleReader reader;

    @Override
    public void execute() {
        try {
            System.out.print("Enter room number: ");
            int number = reader.nextInt();

            System.out.print("Enter capacity: ");
            int capacity = reader.nextInt();

            System.out.print("Enter stars: ");
            int stars = reader.nextInt();

            System.out.print("Enter price per night: ");
            double price = reader.nextDouble();

            Room room = new Room(null, number, capacity, stars, price);
            admin.addRoom(room);

            System.out.println("Room successfully added.");

        } catch (RoomException e) {
            System.out.println("Failed to add room: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Invalid input.");
        }
    }
}
