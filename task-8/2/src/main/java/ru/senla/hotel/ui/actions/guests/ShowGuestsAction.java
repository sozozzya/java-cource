package ru.senla.hotel.ui.actions.guests;

import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.management.Administrator;
import ru.senla.hotel.ui.menu.IAction;

public class ShowGuestsAction implements IAction {

    @Inject
    private Administrator admin;

    private final String mode;

    public ShowGuestsAction(String mode) {
        this.mode = mode;
    }

    @Override
    public void execute() {
        if ("name".equals(mode)) admin.printGuestsByName();
        else admin.printGuestsByCheckoutDate();
    }
}
