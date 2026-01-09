package ru.senla.hotel.ui.builder;

import ru.senla.hotel.management.Administrator;
import ru.senla.hotel.ui.menu.Menu;

public class Builder {
    private final ConsoleMenuFactory factory;

    public Builder(Administrator admin) {
        this.factory = new ConsoleMenuFactory(admin);
    }

    public Menu buildRootMenu() {
        return factory.createRootMenu();
    }
}
