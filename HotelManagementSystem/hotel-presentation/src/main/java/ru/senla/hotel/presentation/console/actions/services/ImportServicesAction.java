package ru.senla.hotel.presentation.console.actions.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.senla.hotel.service.ServiceManager;
import ru.senla.hotel.service.exception.service.ServiceException;
import ru.senla.hotel.presentation.console.actions.IAction;
import ru.senla.hotel.presentation.console.util.ConsoleReader;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ImportServicesAction implements IAction {

    private static final Logger log = LoggerFactory.getLogger(ImportServicesAction.class);

    private final ServiceManager serviceManager;
    private final ConsoleReader reader;

    @Autowired
    public ImportServicesAction(ServiceManager serviceManager,
                                ConsoleReader reader) {
        this.serviceManager = serviceManager;
        this.reader = reader;
    }

    @Override
    public void execute() {
        log.info("User command started: IMPORT_SERVICES");

        try {
            System.out.print("Enter CSV file path to import services: ");
            String path = reader.nextLine();

            serviceManager.importServiceFromCSV(path);

            log.info("User command completed successfully: IMPORT_SERVICES, path={}", path);
            System.out.println("Services imported successfully.");
        } catch (ServiceException e) {
            log.error("User command failed: IMPORT_SERVICES", e);
            System.out.println("Failed to import services: " + e.getMessage());
        }
    }
}
