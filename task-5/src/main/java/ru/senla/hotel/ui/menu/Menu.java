package ru.senla.hotel.ui.menu;

public class Menu {
    private final String name;
    private final MenuItem[] menuItems;

    public Menu(String name, MenuItem[] items) {
        this.name = name;
        this.menuItems = items;
    }

    public String getName() {
        return name;
    }

    public MenuItem[] getMenuItems() {
        return menuItems;
    }
}