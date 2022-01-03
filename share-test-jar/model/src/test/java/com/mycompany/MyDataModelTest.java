package com.mycompany;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class MyDataModelTest {

    public static List<MyDataModel> testData = ImmutableList.of(
            new MyDataModel("1", "2"),
            new MyDataModel("A", "B"),
            new MyDataModel("X", "Y")
    );

    @Test
    public void testMyDataObjects() {
        assertEquals(3, testData.size());
        assertEquals("1", testData.get(0).getData1());
    }
}
