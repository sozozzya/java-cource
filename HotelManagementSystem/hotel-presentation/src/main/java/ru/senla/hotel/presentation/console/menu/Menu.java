package ru.senla.hotel.presentation.console.menu;

import java.util.Collections;
import java.util.List;

public class Menu {
    private final String name;
    private final List<MenuItem> menuItems;

    public Menu(String name, List<MenuItem> items) {
        this.name = name;
        this.menuItems = Collections.unmodifiableList(items);
    }

    public String getName() {
        return name;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }
}