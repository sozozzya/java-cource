package ru.senla.hotel.ui.builder;

import ru.senla.hotel.di.annotation.Component;
import ru.senla.hotel.di.annotation.Inject;
import ru.senla.hotel.ui.menu.Menu;

@Component
public class Builder {
    @Inject
    private AbstractMenuFactory factory;

    public Menu buildRootMenu() {
        return factory.createRootMenu();
    }
}
