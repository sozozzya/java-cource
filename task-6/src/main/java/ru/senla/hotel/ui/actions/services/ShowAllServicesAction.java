package ru.senla.hotel.ui.actions.services;

import ru.senla.hotel.management.Administrator;
import ru.senla.hotel.ui.menu.IAction;

public class ShowAllServicesAction implements IAction {
    private final Administrator admin;

    public ShowAllServicesAction(Administrator admin) {
        this.admin = admin;
    }

    @Override
    public void execute() {
        admin.printAllServices();
    }
}
