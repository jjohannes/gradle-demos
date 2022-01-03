package com.mycompany;

import java.util.List;

public class MyService {

    public boolean doWork(List<MyDataModel> data) {
        for (MyDataModel entry : data) {
            System.out.println("Working on: " + entry);
        }
        return true;
    }
}
