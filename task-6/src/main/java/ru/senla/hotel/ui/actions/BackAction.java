package ru.senla.hotel.ui.actions;

import ru.senla.hotel.ui.menu.IAction;

public class BackAction implements IAction {
    @Override
    public void execute() {
        System.out.println("Returning to previous menu...");
    }
}
