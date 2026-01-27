package ru.senla.hotel.ui.actions.reports;

import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.di.annotation.Scope;
import ru.senla.hotel.management.Administrator;
import ru.senla.hotel.ui.menu.IAction;

@Component(scope = Scope.PROTOTYPE)
public class ShowAllPricesAction implements IAction {

    @Inject
    private Administrator admin;

    @Override
    public void execute() {
        admin.printAllPrices();
    }
}
