package ru.senla.hotel.ui.actions.reports;

import ru.senla.hotel.management.Administrator;
import ru.senla.hotel.ui.menu.IAction;

public class ShowAllPricesAction implements IAction {
    private final Administrator admin;

    public ShowAllPricesAction(Administrator admin) {
        this.admin = admin;
    }

    @Override
    public void execute() {
        admin.printAllPrices();
    }
}
