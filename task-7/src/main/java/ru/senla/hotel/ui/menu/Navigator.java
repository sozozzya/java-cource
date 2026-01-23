package ru.senla.hotel.ui.menu;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class Navigator {
    private Menu currentMenu;
    private final Deque<Menu> history = new ArrayDeque<>();

    public Navigator(Menu rootMenu) {
        this.currentMenu = rootMenu;
    }

    public void printMenu() {
        System.out.println("\n=== " + currentMenu.getName() + " ===");
        List<MenuItem> items = currentMenu.getMenuItems();
        for (int i = 0; i < items.size(); i++) {
            System.out.println((i + 1) + ". " + items.get(i).getTitle());
        }
        System.out.print("Select option (number): ");
    }

    public void navigate(int index) {
        List<MenuItem> items = currentMenu.getMenuItems();

        if (index < 1 || index > items.size()) {
            System.out.println("Invalid selection. Please choose a valid menu item.");
            return;
        }

        MenuItem item = items.get(index - 1);
        try {
            item.doAction();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

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
