package com.epam.rd.java.basic.practice5;

import java.util.logging.Logger;
import java.util.stream.Stream;

public class Part3 {
    private static final Logger LOGGER = Logger.getLogger(Part3.class.getSimpleName());

    private final int numberOfThreads;
    private final int numberOfIterations;
    private int counter;
    private int counter2;

    public Part3(int numberOfThreads, int numberOfIterations) {
        this.numberOfThreads = numberOfThreads;
        this.numberOfIterations = numberOfIterations;
    }

    public static void main(final String[] args) {
        Part3 part3 = new Part3(2, 10);

        System.out.println("Compare without synchronization");
        part3.compare();

        part3.refreshCounters();

        System.out.println(System.lineSeparator() + "Compare with synchronization");
        part3.compareSync();

    }

    private void refreshCounters() {
        counter = counter2 = 0;
    }

    public void compare() {
        Thread[] threads = new Thread[numberOfThreads];
        runParallelComparison(threads, false);
    }

    public void compareSync() {
        Thread[] threads = new Thread[numberOfThreads];
        runParallelComparison(threads, true);
    }

    private void runParallelComparison(Thread[] threads, boolean sync) {
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                if (sync) {
                    Stream.iterate(0, j -> j + 1)
                            .limit(numberOfIterations)
                            .forEach(j -> {
                                synchronized (this) {
                                    compareCountersAndIncrement();
                                }
                            });
                } else {
                    Stream.iterate(0, j -> j + 1)
                            .limit(numberOfIterations)
                            .forEach(j -> compareCountersAndIncrement());
                }
            });
            threads[i].start();
        }
        try {
            for (Thread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            LOGGER.warning("The main thread was interrupted");
            Thread.currentThread().interrupt();
        }
    }

    private void compareCountersAndIncrement() {
        try {
            System.out.println(counter == counter2);
            counter++;
            Thread.sleep(100);
            counter2++;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}