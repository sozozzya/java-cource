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
        return scanner.nextLine();
    }

    public int nextInt() {
        try {
            return Integer.parseInt(nextLine().trim());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid integer input");
        }
    }

    public double nextDouble() {
        try {
            return Double.parseDouble(nextLine().trim());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid double input");
        }
    }
}
