package ru.senla.hotel.ui.actions.services;

import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.di.annotation.Scope;
import ru.senla.hotel.exception.service.ServiceException;
import ru.senla.hotel.management.Administrator;
import ru.senla.hotel.ui.menu.IAction;
import ru.senla.hotel.ui.util.ConsoleReader;

@Component(scope = Scope.PROTOTYPE)
public class ChangeServicePriceAction implements IAction {

    @Inject
    private Administrator admin;

    @Inject
    private ConsoleReader reader;

    @Override
    public void execute() {
        try {
            System.out.print("Enter service name: ");
            String name = reader.nextLine();

            System.out.print("Enter new price: ");
            double price = reader.nextDouble();

            admin.changeServicePrice(name, price);
            System.out.println("Service price updated successfully.");

        } catch (ServiceException e) {
            System.out.println("Failed to change service price: " + e.getMessage());
        }
    }
}
