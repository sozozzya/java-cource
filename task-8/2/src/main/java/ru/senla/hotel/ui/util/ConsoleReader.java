package ru.senla.hotel.ui.util;

import java.util.Scanner;

public class ConsoleReader {
    private static ConsoleReader instance;
    private final Scanner scanner = new Scanner(System.in);

    private ConsoleReader() {
    }

    public static ConsoleReader getInstance() {
        if (instance == null) {
            instance = new ConsoleReader();
        }
        return instance;
    }

    public String nextLine() {
        return scanner.nextLine().trim();
    }

    public int nextInt() {
        while (true) {
            try {
                return Integer.parseInt(nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid integer: ");
            }
        }
    }

    public double nextDouble() {
        while (true) {
            try {
                return Double.parseDouble(nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }

    public boolean nextYesNo() {
        while (true) {
            String input = nextLine().toLowerCase();
            if (input.equals("yes") || input.equals("y")) return true;
            if (input.equals("no") || input.equals("n")) return false;

            System.out.print("Please enter yes or no: ");
        }
    }
}
