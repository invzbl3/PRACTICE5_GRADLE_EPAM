package com.epam.rd.java.basic.practice5;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Part1 {
    private static final Logger LOGGER = Logger.getLogger(Part1.class.getName());

    public static void main(String[] args) {
        BasicThread basicThread = new BasicThread();
        RunnableThread runnableThread = new RunnableThread();

        try {
            basicThread.start();
            basicThread.join();
            Thread.sleep(500);
            runnableThread.run();
        } catch (InterruptedException ex) {
            LOGGER.log(Level.SEVERE, "Interrupted exception", ex);
            Thread.currentThread().interrupt();
        }
    }

    static class BasicThread extends Thread {
        @Override
        public synchronized void run() {
            runThread();
        }

        private static void runThread() {
            BasicThread basicThread = new BasicThread();

            System.out.println(basicThread.getName());
            for (int i = 0; i < 3; i++) {
                try {
                    Thread.sleep(500);
                    System.out.println(basicThread.getName());
                } catch (InterruptedException e) {
                    LOGGER.log(Level.SEVERE, "Interrupted exception", e);
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    static class RunnableThread implements Runnable {
        @Override
        public synchronized void run() {
            BasicThread.runThread();
        }
    }
}