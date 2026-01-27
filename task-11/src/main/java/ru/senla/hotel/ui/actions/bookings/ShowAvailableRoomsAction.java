package ru.senla.hotel.ui.actions.bookings;

import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.management.Administrator;
import ru.senla.hotel.ui.menu.IAction;

public class ShowAvailableRoomsAction implements IAction {

    @Inject
    private Administrator admin;

    private final String by;

    public ShowAvailableRoomsAction(String by) {
        this.by = by;
    }

    @Override
    public void execute() {
        switch (by) {
            case "capacity" -> admin.printAvailableRoomsByCapacity();
            case "stars" -> admin.printAvailableRoomsByStars();
            default -> admin.printAvailableRoomsByPrice();
        }
    }
}
