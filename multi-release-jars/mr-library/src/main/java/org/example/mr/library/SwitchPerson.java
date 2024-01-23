package org.example.mr.library;

public final class SwitchPerson {
    public static String getPersonType(Person person) {
        System.out.println("Java 17 version!");
        if (person instanceof Customer)
            return "Customer";
        else if (person instanceof Employee)
            return "Employee";
        return "Unknown";
    }
}
