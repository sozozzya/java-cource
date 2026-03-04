package ru.senla.hotel.ui.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.di.annotation.Scope;
import ru.senla.hotel.ui.menu.IAction;

@Component(scope = Scope.PROTOTYPE)
public class BackAction implements IAction {

    private static final Logger log = LoggerFactory.getLogger(BackAction.class);

    @Override
    public void execute() {
        log.info("User command started: BACK");

        System.out.println("Returning to previous menu...");

        log.info("User command completed successfully: BACK");
    }
}
