package ru.senla.hotel.presentation.console.menu;

import ru.senla.hotel.presentation.console.actions.IAction;

import java.util.Optional;
import java.util.function.Supplier;

public class MenuItem {

    private final String title;
    private final Supplier<IAction> actionSupplier;
    private final Menu nextMenu;
    private final boolean isBack;

    public MenuItem(String title, Supplier<IAction> actionSupplier, Menu nextMenu) {
        this(title, actionSupplier, nextMenu, false);
    }

    public MenuItem(String title, Supplier<IAction> actionSupplier) {
        this(title, actionSupplier, null, true);
    }

    private MenuItem(String title,
                     Supplier<IAction> actionSupplier,
                     Menu nextMenu,
                     boolean isBack) {
        this.title = title;
        this.actionSupplier = actionSupplier;
        this.nextMenu = nextMenu;
        this.isBack = isBack;
    }

    public String getTitle() {
        return title;
    }

    public Menu getNextMenu() {
        return nextMenu;
    }

    public boolean isBack() {
        return isBack;
    }

    public void doAction() {
        Optional.ofNullable(actionSupplier)
                .map(Supplier::get)
                .ifPresent(IAction::execute);
    }
}
