package ru.senla.hotel.presentation.console.util;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Scanner;

@Component
public class ConsoleReader {
    private final Scanner scanner = new Scanner(System.in);

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

    public BigDecimal nextBigDecimal() {
        while (true) {
            try {
                return new BigDecimal(nextLine().replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid decimal number: ");
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
