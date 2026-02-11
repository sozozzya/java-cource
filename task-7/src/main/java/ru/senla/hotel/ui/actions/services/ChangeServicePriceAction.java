package ru.senla.hotel.ui.actions.services;

import ru.senla.hotel.exception.service.ServiceException;
import ru.senla.hotel.management.Administrator;
import ru.senla.hotel.ui.menu.IAction;
import ru.senla.hotel.ui.util.ConsoleReader;

public class ChangeServicePriceAction implements IAction {
    private final Administrator admin;

    public ChangeServicePriceAction(Administrator admin) {
        this.admin = admin;
    }

    @Override
    public void execute() {
        try {
            ConsoleReader reader = ConsoleReader.getInstance();
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
