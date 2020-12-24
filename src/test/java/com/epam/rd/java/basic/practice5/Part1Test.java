package com.epam.rd.java.basic.practice5;

import org.junit.Assert;
import org.junit.Test;

public class Part1Test {
    @Test
    public void basicThreadTest(){
        Thread thread = new Part1.BasicThread();
        Assert.assertNotNull(thread);
    }

    @Test
    public void runnableThreadTest(){
        Thread thread = new Thread(new Part1.RunnableThread());
        Assert.assertNotNull(thread);
    }
}