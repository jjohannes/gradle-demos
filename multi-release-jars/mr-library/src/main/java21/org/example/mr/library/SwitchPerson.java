package org.example.mr.library;

import org.example.mr.library.Customer;
import org.example.mr.library.Employee;
import org.example.mr.library.Person;

public final class SwitchPerson {
    public static String getPersonType(Person person) {
        System.out.println("Java 21 version!");
        return switch (person) {
            case Customer customer -> "Customer";
            case Employee employee -> "Employee";
        };
    }
}
