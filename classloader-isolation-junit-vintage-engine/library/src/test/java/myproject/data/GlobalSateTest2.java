package myproject.data;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GlobalSateTest2 {

    @Test
    public void testStateA() {
        System.out.println(Singleton.GLOBAL_STATE);
        assertEquals(Singleton.GLOBAL_STATE, "UNSET");
        Singleton.GLOBAL_STATE = "testStateA";
    }

    @Test
    public void testStateB() {
        System.out.println(Singleton.GLOBAL_STATE);
        assertEquals(Singleton.GLOBAL_STATE, "testStateA");
        Singleton.GLOBAL_STATE = "testStateB";
    }
}
