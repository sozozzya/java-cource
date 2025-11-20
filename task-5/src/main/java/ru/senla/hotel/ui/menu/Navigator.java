package ru.senla.hotel.ui.menu;

import java.util.Stack;

public class Navigator {

    private Menu currentMenu;
    private final Stack<Menu> history = new Stack<>();

    public Navigator(Menu rootMenu) {
        this.currentMenu = rootMenu;
    }

    public void printMenu() {
        System.out.println("\n=== " + currentMenu.getName() + " ===");
        MenuItem[] items = currentMenu.getMenuItems();
        for (int i = 0; i < items.length; i++) {
            System.out.println((i + 1) + ". " + items[i].getTitle());
        }
        System.out.print("Select option (number): ");
    }

    public void navigate(int index) {
        MenuItem[] items = currentMenu.getMenuItems();

        if (index < 1 || index > items.length) {
            System.out.println("Invalid selection.");
            return;
        }

        MenuItem item = items[index - 1];

        item.doAction();

        if (item.isBack()) {
            if (!history.isEmpty()) {
                currentMenu = history.pop();
            }
            return;
        }

        if (item.getNextMenu() != null) {
            history.push(currentMenu);
            currentMenu = item.getNextMenu();
        }
    }
}
