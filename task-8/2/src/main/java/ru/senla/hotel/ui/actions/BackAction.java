package ru.senla.hotel.ui.actions;

import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.di.annotation.Scope;
import ru.senla.hotel.ui.menu.IAction;

@Component(scope = Scope.PROTOTYPE)
public class BackAction implements IAction {
    @Override
    public void execute() {
        System.out.println("Returning to previous menu...");
    }
}
