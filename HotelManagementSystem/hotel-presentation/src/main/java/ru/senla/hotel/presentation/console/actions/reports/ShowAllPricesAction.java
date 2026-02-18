package ru.senla.hotel.presentation.console.actions.reports;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.senla.hotel.domain.exception.DomainException;
import ru.senla.hotel.presentation.console.ConsoleReportPrinter;
import ru.senla.hotel.presentation.console.actions.IAction;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ShowAllPricesAction implements IAction {

    private static final Logger log = LoggerFactory.getLogger(ShowAllPricesAction.class);

    private final ConsoleReportPrinter consoleReportPrinter;

    @Autowired
    public ShowAllPricesAction(ConsoleReportPrinter consoleReportPrinter) {
        this.consoleReportPrinter = consoleReportPrinter;
    }

    @Override
    public void execute() {
        log.info("User command started: SHOW_ALL_PRICES");

        try {
            consoleReportPrinter.printAllPrices();

            log.info("User command completed successfully: SHOW_ALL_PRICES");
        } catch (DomainException e) {
            log.error("User command failed: SHOW_ALL_PRICES", e);
            System.out.println("Failed to show prices: " + e.getMessage());
        }
    }
}
