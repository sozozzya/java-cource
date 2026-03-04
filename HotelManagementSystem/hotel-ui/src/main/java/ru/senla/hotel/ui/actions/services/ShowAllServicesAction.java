package ru.senla.hotel.ui.actions.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.di.annotation.Scope;
import ru.senla.hotel.service.Administrator;
import ru.senla.hotel.service.exception.service.ServiceException;
import ru.senla.hotel.ui.menu.IAction;

@Component(scope = Scope.PROTOTYPE)
public class ShowAllServicesAction implements IAction {

    @Inject
    private Administrator admin;

    private static final Logger log = LoggerFactory.getLogger(ShowAllServicesAction.class);

    @Override
    public void execute() {
        log.info("User command started: SHOW_ALL_SERVICES");

        try {
            admin.printAllServices();

            log.info("User command completed successfully: SHOW_ALL_SERVICES");
        } catch (ServiceException e) {
            log.error("User command failed: SHOW_ALL_SERVICES", e);
            System.out.println("Failed to show services: " + e.getMessage());
        }
    }
}
