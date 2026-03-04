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

import java.math.BigDecimal;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ChangeServicePriceAction implements IAction {

    private static final Logger log = LoggerFactory.getLogger(ChangeServicePriceAction.class);

    private final ServiceManager serviceManager;
    private final ConsoleReader reader;

    @Autowired
    public ChangeServicePriceAction(ServiceManager serviceManager,
                                    ConsoleReader reader) {
        this.serviceManager = serviceManager;
        this.reader = reader;
    }

    @Override
    public void execute() {
        log.info("User command started: CHANGE_SERVICE_PRICE");

        try {
            System.out.print("Enter service name: ");
            String name = reader.nextLine();

            System.out.print("Enter new price: ");
            BigDecimal price = reader.nextBigDecimal();

            serviceManager.changeServicePrice(name, price);

            log.info("User command completed successfully: CHANGE_SERVICE_PRICE, name={}", name);
            System.out.println("Service price updated successfully.");
        } catch (ServiceException e) {
            log.error("User command failed: CHANGE_SERVICE_PRICE", e);
            System.out.println("Failed to change service price: " + e.getMessage());
        }
    }
}
