package ru.senla.hotel.ui.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.senla.hotel.ui.menu.IAction;
import ru.senla.hotel.ui.menu.Menu;

public class NavigateAction implements IAction {

    private final Menu menu;

    public NavigateAction(Menu menu) {
        this.menu = menu;
    }

    private static final Logger log = LoggerFactory.getLogger(NavigateAction.class);

    @Override
    public void execute() {
        log.info("User command started: NAVIGATE_MENU, target={}", menu.getName());

        System.out.println("Opening " + menu.getName() + " menu...");

        log.info("User command completed successfully: NAVIGATE_MENU, target={}", menu.getName());
    }
}
