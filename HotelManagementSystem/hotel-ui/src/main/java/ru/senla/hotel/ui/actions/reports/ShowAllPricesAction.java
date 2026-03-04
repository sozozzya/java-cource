package ru.senla.hotel.ui.actions.reports;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.di.annotation.Scope;
import ru.senla.hotel.model.exception.HotelException;
import ru.senla.hotel.service.Administrator;
import ru.senla.hotel.ui.menu.IAction;

@Component(scope = Scope.PROTOTYPE)
public class ShowAllPricesAction implements IAction {

    @Inject
    private Administrator admin;

    private static final Logger log = LoggerFactory.getLogger(ShowAllPricesAction.class);

    @Override
    public void execute() {
        log.info("User command started: SHOW_ALL_PRICES");

        try {
            admin.printAllPrices();

            log.info("User command completed successfully: SHOW_ALL_PRICES");
        } catch (HotelException e) {
            log.error("User command failed: SHOW_ALL_PRICES", e);
            System.out.println("Failed to show prices: " + e.getMessage());
        }
    }
}
