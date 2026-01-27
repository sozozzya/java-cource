package org;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main extends Thread {

    private final int seconds;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public Main(int seconds) {
        this.seconds = seconds;
        setDaemon(true);
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("Текущее время: " + LocalDateTime.now().format(formatter));
            try {
                Thread.sleep(seconds * 1000L);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int n = 2;

        Thread timeThread = new Main(n);
        timeThread.start();

        Thread.sleep(10_000);
        System.out.println("Главный поток завершён");
    }
}
