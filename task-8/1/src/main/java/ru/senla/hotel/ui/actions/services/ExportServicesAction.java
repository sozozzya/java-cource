package ru.senla.hotel.ui.actions.services;

import ru.senla.hotel.exception.service.ServiceException;
import ru.senla.hotel.management.Administrator;
import ru.senla.hotel.ui.menu.IAction;
import ru.senla.hotel.ui.util.ConsoleReader;

public class ExportServicesAction implements IAction {

    private final Administrator admin;

    public ExportServicesAction(Administrator admin) {
        this.admin = admin;
    }

    @Override
    public void execute() {
        try {
            ConsoleReader reader = ConsoleReader.getInstance();

            System.out.print("Enter CSV file path to export services: ");
            String path = reader.nextLine();

            admin.exportServices(path);
            System.out.println("Services exported successfully.");

        } catch (ServiceException e) {
            System.out.println("Failed to export services: " + e.getMessage());
        }
    }
}
