package ru.senla.hotel.ui.actions;

import ru.senla.hotel.ui.menu.IAction;

public class ExitAction implements IAction {
    @Override
    public void execute() {
        System.out.println("Goodbye!");
        System.exit(0);
    }
}
