package com.epam.rd.java.basic.practice5;

import java.io.*;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Part5 {
    private static final String IOEXCEPTION = "IOException";
    private static final String INTERRUPTED_EXCEPTION = "Interrupted exception";
    private static final Logger LOGGER = Logger.getLogger(Part5.class.getName());
    static final int THREADS_COUNT = 10;
    static final int SYMBOLS_COUNT = 20;
    static Thread[] threads = new Thread[THREADS_COUNT];
    static long currentPosition = 1;
    static String ls = System.lineSeparator();
    private static final String FILE_NAME = "part5.txt";

    public static void main(final String[] args) {

        try {
            Files.deleteIfExists(new File(FILE_NAME).toPath());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, IOEXCEPTION, e);
        }
        try (RandomAccessFile file = new RandomAccessFile(FILE_NAME, "rw")){
            for (int k = 0; k < THREADS_COUNT; k++) {
                int finalK = k;
                Thread t2 = new Thread(() -> {
                    synchronized (file) {
                        try {
                            threadJob(file, finalK);

                        } catch (IOException e) {
                            LOGGER.log(Level.SEVERE, IOEXCEPTION, e);
                        }
                    }
                });

                threads[k] = t2;
                t2.start();
            }
            joinThread();

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, IOEXCEPTION, e);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            StringBuilder outputStringSB = new StringBuilder();
            String str;
            while (((str = reader.readLine()) != null)) {
                outputStringSB.append(str).append(ls);
            }
            System.out.print(outputStringSB.toString());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, IOEXCEPTION, e);
        }

    }

    private static void joinThread() {
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, INTERRUPTED_EXCEPTION, e);
                Thread.currentThread().interrupt();
            }
        }
    }

    private static void threadJob( RandomAccessFile file, int k) throws IOException {

        currentPosition = (SYMBOLS_COUNT+ls.length()) * (long)k;

        for (int i = 0; i < SYMBOLS_COUNT ; i++) {
            file.seek(currentPosition);
            file.write(Integer.toString(k).getBytes());
            currentPosition++;
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, INTERRUPTED_EXCEPTION, e);
                Thread.currentThread().interrupt();
            }
        }
        file.write(ls.getBytes());
        file.seek(currentPosition+ls.length());
    }
}