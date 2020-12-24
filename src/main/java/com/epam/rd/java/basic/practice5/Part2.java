package com.epam.rd.java.basic.practice5;

import java.io.ByteArrayInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.InputStream;

public class Part2 {
    private static final Logger LOGGER = Logger.getLogger(Part2.class.getName());
    private static final String INTERRUPTED_EXCEPTION = "Interrupted exception";
    private static final InputStream STD_IN = System.in;
    private static final InputStream YOUR_OWN_INPUT_STREAM = new ByteArrayInputStream(new byte[]{'\n'});

    public static void main(String[] args) {

        System.setIn(YOUR_OWN_INPUT_STREAM);
        Thread t = new Thread(() -> Spam.main(null));
        t.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            LOGGER.log(Level.SEVERE, INTERRUPTED_EXCEPTION, e);
            Thread.currentThread().interrupt();
        }

        try {
            t.join();
        } catch (InterruptedException e) {
            LOGGER.log(Level.SEVERE, INTERRUPTED_EXCEPTION, e);
            Thread.currentThread().interrupt();
        }
        System.setIn(STD_IN);
    }
}