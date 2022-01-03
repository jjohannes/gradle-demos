package com.mycompany;

import org.junit.Test;

import static org.junit.Assert.*;

public class MyServiceTest {

    @Test
    public void testService() {
        assertTrue(new MyService().doWork(MyDataModelTest.testData));
    }
}
