package ru.senla.hotel.presentation.console.actions.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.senla.hotel.service.ServiceManager;
import ru.senla.hotel.service.exception.service.ServiceException;
import ru.senla.hotel.domain.model.HotelService;
import ru.senla.hotel.presentation.console.actions.IAction;
import ru.senla.hotel.presentation.console.util.ConsoleReader;

import java.math.BigDecimal;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AddServiceAction implements IAction {

    private static final Logger log = LoggerFactory.getLogger(AddServiceAction.class);

    private final ServiceManager serviceManager;
    private final ConsoleReader reader;

    @Autowired
    public AddServiceAction(ServiceManager serviceManager,
                            ConsoleReader reader) {
        this.serviceManager = serviceManager;
        this.reader = reader;
    }

    @Override
    public void execute() {
        log.info("User command started: ADD_SERVICE");

        try {
            System.out.print("Enter service name: ");
            String name = reader.nextLine();

            System.out.print("Enter price: ");
            BigDecimal price = reader.nextBigDecimal();

            serviceManager.addService(new HotelService(name, price));

            log.info("User command completed successfully: ADD_SERVICE, name={}", name);
            System.out.println("Service successfully added.");
        } catch (ServiceException e) {
            log.error("User command failed: ADD_SERVICE", e);
            System.out.println("Failed to add service: " + e.getMessage());
        } catch (Exception e) {
            log.error("User command failed due to invalid input: ADD_SERVICE", e);
            System.out.println("Invalid input.");
        }
    }
}
