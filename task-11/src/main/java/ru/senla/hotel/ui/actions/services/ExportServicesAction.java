package ru.senla.hotel.ui.actions.services;

import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.di.annotation.Scope;
import ru.senla.hotel.exception.service.ServiceException;
import ru.senla.hotel.management.Administrator;
import ru.senla.hotel.ui.menu.IAction;
import ru.senla.hotel.ui.util.ConsoleReader;

@Component(scope = Scope.PROTOTYPE)
public class ExportServicesAction implements IAction {

    @Inject
    private Administrator admin;

    @Inject
    private ConsoleReader reader;

    @Override
    public void execute() {
        try {
            System.out.print("Enter CSV file path to export services: ");
            String path = reader.nextLine();

            admin.exportServices(path);
            System.out.println("Services exported successfully.");

        } catch (ServiceException e) {
            System.out.println("Failed to export services: " + e.getMessage());
        }
    }
}
