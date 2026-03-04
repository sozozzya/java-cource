package ru.senla.hotel.ui.actions.rooms;

import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.management.Administrator;
import ru.senla.hotel.ui.menu.IAction;

public class ShowAllRoomsAction implements IAction {

    @Inject
    private Administrator admin;

    private final String sortBy;

    public ShowAllRoomsAction(String sortBy) {
        this.sortBy = sortBy;
    }

    @Override
    public void execute() {
        switch (sortBy) {
            case "capacity" -> admin.printAllRoomsByCapacity();
            case "stars" -> admin.printAllRoomsByStars();
            default -> admin.printAllRoomsByPrice();
        }
    }
}
