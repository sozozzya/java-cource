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
public class ExportServicesAction implements IAction {

    private static final Logger log = LoggerFactory.getLogger(ExportServicesAction.class);

    private final ServiceManager serviceManager;
    private final ConsoleReader reader;

    @Autowired
    public ExportServicesAction(ServiceManager serviceManager,
                                ConsoleReader reader) {
        this.serviceManager = serviceManager;
        this.reader = reader;
    }

    @Override
    public void execute() {
        log.info("User command started: EXPORT_SERVICES");

        try {
            System.out.print("Enter CSV file path to export services: ");
            String path = reader.nextLine();

            serviceManager.exportServiceToCSV(path);

            log.info("User command completed successfully: EXPORT_SERVICES, path={}", path);
            System.out.println("Services exported successfully.");
        } catch (ServiceException e) {
            log.error("User command failed: EXPORT_SERVICES", e);
            System.out.println("Failed to export services: " + e.getMessage());
        }
    }
}
