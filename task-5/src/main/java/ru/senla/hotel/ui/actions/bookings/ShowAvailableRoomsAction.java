package ru.senla.hotel.ui.actions.bookings;

import ru.senla.hotel.management.Administrator;
import ru.senla.hotel.ui.menu.IAction;

public class ShowAvailableRoomsAction implements IAction {
    private final Administrator admin;
    private final String by;

    public ShowAvailableRoomsAction(Administrator admin, String by) {
        this.admin = admin;
        this.by = by;
    }

    @Override
    public void execute() {
        switch (by) {
            case "price" -> admin.printAvailableRoomsByPrice();
            case "capacity" -> admin.printAvailableRoomsByCapacity();
            case "stars" -> admin.printAvailableRoomsByStars();
            default -> admin.printAvailableRoomsByPrice();
        }
    }
}
