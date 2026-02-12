package ru.senla.hotel.ui.actions.services;

import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.di.annotation.Scope;
import ru.senla.hotel.exception.service.ServiceException;
import ru.senla.hotel.management.Administrator;
import ru.senla.hotel.model.Service;
import ru.senla.hotel.ui.menu.IAction;
import ru.senla.hotel.ui.util.ConsoleReader;

@Component(scope = Scope.PROTOTYPE)
public class AddServiceAction implements IAction {

    @Inject
    private Administrator admin;

    @Inject
    private ConsoleReader reader;

    @Override
    public void execute() {
        try {
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
