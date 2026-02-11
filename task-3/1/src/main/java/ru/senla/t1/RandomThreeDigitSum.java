package ru.senla.t1;

import java.util.Random;

public class RandomThreeDigitSum {
    public static void main(String[] args) {
        int number = 100 + (new Random()).nextInt(900);

        int hundreds = number / 100;
        int tens = (number / 10) % 10;
        int ones = number % 10;

        int sum = hundreds + tens + ones;

        System.out.println("A randomly generated three-digit natural number: " + number);
        System.out.println("The sum of its digits: " + sum);
    }
}
