package com.mycompany;

public class MyDataModel {

    private String data1;
    private String data2;

    public MyDataModel(String data1, String data2) {
        this.data1 = data1;
        this.data2 = data2;
    }

    public String getData1() {
        return data1;
    }

    public String getData2() {
        return data2;
    }

    @Override
    public String toString() {
        return data1 + ", " + data2;
    }
}
