package ru.senla.hotel.presentation.console.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ExitAction implements IAction {

    private static final Logger log = LoggerFactory.getLogger(ExitAction.class);

    @Override
    public void execute() {
        log.info("User command: EXIT");
        System.out.println("Goodbye!");
        System.exit(0);
    }
}
