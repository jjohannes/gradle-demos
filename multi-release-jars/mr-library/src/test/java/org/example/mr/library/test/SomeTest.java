package org.example.mr.library.test;

import org.example.mr.library.Customer;
import org.example.mr.library.SwitchPerson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SomeTest {
    @Test
    void smoke_test() {
        Assertions.assertEquals("Customer", SwitchPerson.getPersonType(new Customer("John Doe")));
    }
}
