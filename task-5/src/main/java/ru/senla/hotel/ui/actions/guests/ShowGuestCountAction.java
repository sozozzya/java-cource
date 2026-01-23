package ru.senla.hotel.ui.actions.guests;

import ru.senla.hotel.management.Administrator;
import ru.senla.hotel.ui.menu.IAction;

public class ShowGuestCountAction implements IAction {
    private final Administrator admin;

    public ShowGuestCountAction(Administrator admin) {
        this.admin = admin;
    }

    @Override
    public void execute() {
        admin.printGuestCount();
    }
}
