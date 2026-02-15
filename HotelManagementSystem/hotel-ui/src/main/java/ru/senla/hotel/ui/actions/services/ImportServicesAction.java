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
public class ImportServicesAction implements IAction {

    @Inject
    private Administrator admin;

    @Inject
    private ConsoleReader reader;

    private static final Logger log = LoggerFactory.getLogger(ImportServicesAction.class);

    @Override
    public void execute() {
        log.info("User command started: IMPORT_SERVICES");

        try {
            System.out.print("Enter CSV file path to import services: ");
            String path = reader.nextLine();

            admin.importServices(path);

            log.info("User command completed successfully: IMPORT_SERVICES, path={}", path);
            System.out.println("Services imported successfully.");
        } catch (ServiceException e) {
            log.error("User command failed: IMPORT_SERVICES", e);
            System.out.println("Failed to import services: " + e.getMessage());
        }
    }
}
