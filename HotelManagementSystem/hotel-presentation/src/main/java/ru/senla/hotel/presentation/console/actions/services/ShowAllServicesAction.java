package ru.senla.hotel.presentation.console.actions.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.senla.hotel.presentation.console.ConsoleReportPrinter;
import ru.senla.hotel.service.exception.service.ServiceException;
import ru.senla.hotel.presentation.console.actions.IAction;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ShowAllServicesAction implements IAction {

    private static final Logger log = LoggerFactory.getLogger(ShowAllServicesAction.class);

    @Autowired
    private final ConsoleReportPrinter consoleReportPrinter;

    public ShowAllServicesAction(ConsoleReportPrinter consoleReportPrinter) {
        this.consoleReportPrinter = consoleReportPrinter;
    }

    @Override
    public void execute() {
        log.info("User command started: SHOW_ALL_SERVICES");

        try {
            consoleReportPrinter.printAllServices();

            log.info("User command completed successfully: SHOW_ALL_SERVICES");
        } catch (ServiceException e) {
            log.error("User command failed: SHOW_ALL_SERVICES", e);
            System.out.println("Failed to show services: " + e.getMessage());
        }
    }
}
