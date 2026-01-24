package ru.senla.hotel.ui.actions.bookings;

import ru.senla.hotel.management.Administrator;
import ru.senla.hotel.ui.menu.IAction;

public class ShowAvailableRoomCountAction implements IAction {
    private final Administrator admin;

    public ShowAvailableRoomCountAction(Administrator admin) {
        this.admin = admin;
    }

    @Override
    public void execute() {
        admin.printAvailableRoomCount();
    }
}
