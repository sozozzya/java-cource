package ru.senla.hotel.ui.actions.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.di.annotation.Scope;
import ru.senla.hotel.service.exception.service.ServiceException;
import ru.senla.hotel.service.Administrator;
import ru.senla.hotel.ui.menu.IAction;
import ru.senla.hotel.ui.util.ConsoleReader;

@Component(scope = Scope.PROTOTYPE)
public class ExportServicesAction implements IAction {

    @Inject
    private Administrator admin;

    @Inject
    private ConsoleReader reader;

    private static final Logger log = LoggerFactory.getLogger(ExportServicesAction.class);

    @Override
    public void execute() {
        log.info("User command started: EXPORT_SERVICES");

        try {
            System.out.print("Enter CSV file path to export services: ");
            String path = reader.nextLine();

            admin.exportServices(path);

            log.info("User command completed successfully: EXPORT_SERVICES, path={}", path);
            System.out.println("Services exported successfully.");
        } catch (ServiceException e) {
            log.error("User command failed: EXPORT_SERVICES", e);
            System.out.println("Failed to export services: " + e.getMessage());
        }
    }
}
