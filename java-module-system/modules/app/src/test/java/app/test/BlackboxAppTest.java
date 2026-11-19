package app.test;

import app.App;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BlackboxAppTest {

    @Test
    void testApp() {
        assertEquals("app", App.class.getModule().getName());
        assertEquals("app.test", BlackboxAppTest.class.getModule().getName());
    }
}
