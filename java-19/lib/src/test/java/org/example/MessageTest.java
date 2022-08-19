package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MessageTest {
    @Test
    void java17SealedClass() {
        var hello = new Hello();

        assertFalse(hello.getClass().isSealed());
        assertTrue(hello.getClass().getSuperclass().isSealed());
    }
}

