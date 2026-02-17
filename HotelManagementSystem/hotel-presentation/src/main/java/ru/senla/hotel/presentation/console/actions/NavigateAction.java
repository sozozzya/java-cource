package ru.senla.hotel.presentation.console.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.senla.hotel.presentation.console.menu.Menu;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class NavigateAction implements IAction {

    private static final Logger log = LoggerFactory.getLogger(NavigateAction.class);
    private Menu menu;

    @Autowired
    public NavigateAction() {
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    @Override
    public void execute() {
        log.info("User command started: NAVIGATE_MENU, target={}", menu.getName());
        System.out.println("Opening " + menu.getName() + " menu...");
        log.info("User command completed successfully: NAVIGATE_MENU");
    }
}
