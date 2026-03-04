package ru.senla.hotel.presentation.console.builder;

import org.springframework.stereotype.Component;
import ru.senla.hotel.presentation.console.menu.Menu;

@Component
public class Builder {

    private final AbstractMenuFactory factory;

    public Builder(AbstractMenuFactory factory) {
        this.factory = factory;
    }

    public Menu buildRootMenu() {
        return factory.createRootMenu();
    }
}
