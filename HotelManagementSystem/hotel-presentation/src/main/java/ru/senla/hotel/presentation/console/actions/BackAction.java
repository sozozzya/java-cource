package ru.senla.hotel.presentation.console.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BackAction implements IAction {

    private static final Logger log =
            LoggerFactory.getLogger(BackAction.class);

    @Override
    public void execute() {
        log.info("User command: BACK");
        System.out.println("Returning to previous menu...");
    }
}
