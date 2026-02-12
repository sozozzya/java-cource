package org;

import java.util.concurrent.Semaphore;

public class Main {

    private static final Semaphore s1 = new Semaphore(1);
    private static final Semaphore s2 = new Semaphore(0);

    public static void main(String[] args) {

        Thread t1 = new Thread(() -> print("Thread-1", s1, s2));
        Thread t2 = new Thread(() -> print("Thread-2", s2, s1));

        t1.start();
        t2.start();
    }

    private static void print(String name, Semaphore current, Semaphore next) {
        for (int i = 0; i < 5; ++i) {
            try {
                current.acquire();
                System.out.println(name);
                next.release();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
