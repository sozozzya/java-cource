package ru.senla.hotel.ui.actions.rooms;

import ru.senla.hotel.management.Administrator;
import ru.senla.hotel.ui.menu.IAction;

public class ShowAllRoomsAction implements IAction {
    private final Administrator admin;
    private final String sortBy;

    public ShowAllRoomsAction(Administrator admin, String sortBy) {
        this.admin = admin;
        this.sortBy = sortBy;
    }

    @Override
    public void execute() {
        switch (sortBy) {
            case "price" -> admin.printAllRoomsByPrice();
            case "capacity" -> admin.printAllRoomsByCapacity();
            case "stars" -> admin.printAllRoomsByStars();
            default -> admin.printAllRoomsByPrice();
        }
    }
}
