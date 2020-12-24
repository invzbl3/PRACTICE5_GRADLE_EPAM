package com.epam.rd.java.basic.practice5;

import org.junit.Assert;
import org.junit.Test;

public class DemoTest {
    @Test
    public void demoTest() {
        Demo demo = new Demo();
        Assert.assertNotNull(demo);
    }
}