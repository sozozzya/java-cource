package ru.senla.hotel.ui.actions;

import ru.senla.hotel.ui.menu.IAction;
import ru.senla.hotel.ui.menu.Menu;

public class NavigateAction implements IAction {
    private final Menu menu;

    public NavigateAction(Menu menu) {
        this.menu = menu;
    }

    @Override
    public void execute() {
        System.out.println("Opening " + menu.getName() + " menu...");
    }
}
