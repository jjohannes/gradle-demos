package myproject.data;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GlobalSateTest1 {

    @Test
    public void testState1() {
        System.out.println(Singleton.GLOBAL_STATE);
        assertEquals(Singleton.GLOBAL_STATE, "UNSET");
        Singleton.GLOBAL_STATE = "testState1";
    }

    @Test
    public void testState2() {
        System.out.println(Singleton.GLOBAL_STATE);
        assertEquals(Singleton.GLOBAL_STATE, "testState1");
        Singleton.GLOBAL_STATE = "testState2";
    }
}
