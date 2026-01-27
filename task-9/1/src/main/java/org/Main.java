package org;

public class Main {

    private static final Object LOCK = new Object();

    public static void main(String[] args) throws Exception {

        Thread thread = new Thread(() -> {
            long end = System.currentTimeMillis() + 300;
            while (System.currentTimeMillis() < end) {
            }

            try {
                Thread.sleep(500);

                synchronized (LOCK) {
                    LOCK.wait();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        System.out.println("State after creation: " + thread.getState());

        thread.start();

        Thread.sleep(50);
        System.out.println("State after start: " + thread.getState());

        Thread.sleep(400);
        System.out.println("State during sleep: " + thread.getState());

        synchronized (LOCK) {
            Thread blockedThread = new Thread(() -> {
                synchronized (LOCK) {
                }
            });
            blockedThread.start();
            Thread.sleep(50);
            System.out.println("State while blocked: " + blockedThread.getState());
        }

        Thread.sleep(600);
        System.out.println("State during wait: " + thread.getState());

        synchronized (LOCK) {
            LOCK.notify();
        }

        thread.join();

        System.out.println("State after termination: " + thread.getState());
    }
}
