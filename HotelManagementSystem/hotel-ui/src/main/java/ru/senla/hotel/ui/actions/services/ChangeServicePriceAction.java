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
public class ChangeServicePriceAction implements IAction {

    @Inject
    private Administrator admin;

    @Inject
    private ConsoleReader reader;

    private static final Logger log = LoggerFactory.getLogger(ChangeServicePriceAction.class);

    @Override
    public void execute() {
        log.info("User command started: CHANGE_SERVICE_PRICE");

        try {
            System.out.print("Enter service name: ");
            String name = reader.nextLine();

            System.out.print("Enter new price: ");
            double price = reader.nextDouble();

            admin.changeServicePrice(name, price);

            log.info("User command completed successfully: CHANGE_SERVICE_PRICE, name={}", name);
            System.out.println("Service price updated successfully.");
        } catch (ServiceException e) {
            log.error("User command failed: CHANGE_SERVICE_PRICE", e);
            System.out.println("Failed to change service price: " + e.getMessage());
        }
    }
}
