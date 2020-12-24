package com.epam.rd.java.basic.practice5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class Spam {
    private static final Logger LOGGER = Logger.getLogger(Spam.class.getSimpleName());

    private final Thread[] threads;

    public Spam(final String[] messages, final int[] delays) {
        if (messages == null || delays == null || messages.length != delays.length) {
            throw new IllegalArgumentException("messages and delays must not be null " +
                    "and their lengths must be the same");
        }
        threads = Stream.iterate(0, i -> i + 1)
                .limit(messages.length)
                .map(i -> new Worker(messages[i], delays[i]))
                .toArray(Thread[]::new);
    }

    public static void main(final String[] args) {
        String[] messages = new String[]{"@@@", "bbbbbbb"};
        int[] delays = new int[]{333, 222};
        Spam spam = new Spam(messages, delays);
        spam.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                if ("".equals(line)) {
                    return;
                }
            }
        } catch (IOException e) {
            LOGGER.warning(String.format("unable to read user input: %s", e.getMessage()));
        } finally {
            spam.stop();
            spam.join();
        }
    }

    public void start() {
        for (Thread thread : threads) {
            thread.start();
        }
    }

    public void stop() {
        for (Thread thread : threads) {
            thread.interrupt();
        }
    }

    public void join() {
        try {
            for (Thread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            LOGGER.warning("The main thread was interrupted");
            Thread.currentThread().interrupt();
        }
    }

    private static class Worker extends Thread {
        private final String message;
        private final int delay;

        public Worker(String message, int delay) {
            this.message = message;
            this.delay = delay;
        }

        @Override
        public void run() {
            while (!isInterrupted()) {
                System.out.println(message);
                try {
                    sleep(delay);
                } catch (InterruptedException e) {
                    interrupt();
                }
            }
        }
    }
}