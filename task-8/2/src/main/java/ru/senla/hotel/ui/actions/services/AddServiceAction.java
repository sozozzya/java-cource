package ru.senla.hotel.ui.actions.services;

import ru.senla.hotel.exception.service.ServiceException;
import ru.senla.hotel.management.Administrator;
import ru.senla.hotel.model.Service;
import ru.senla.hotel.ui.menu.IAction;
import ru.senla.hotel.ui.util.ConsoleReader;

public class AddServiceAction implements IAction {
    private final Administrator admin;

    public AddServiceAction(Administrator admin) {
        this.admin = admin;
    }

    @Override
    public void execute() {
        try {
            ConsoleReader reader = ConsoleReader.getInstance();
            System.out.print("Enter service name: ");
            String name = reader.nextLine();
            System.out.print("Enter price: ");
            double price = reader.nextDouble();
            Service service = new Service(name, price);

            admin.addService(service);
            System.out.println("Service successfully added.");

        } catch (ServiceException e) {
            System.out.println("Failed to add service: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Invalid input.");
        }
    }
}
