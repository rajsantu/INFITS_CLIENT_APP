package com.example.infits;


public class SleepData {
    private String date;
    private String hrsSlept;

    public SleepData(String date, String hrsSlept) {
        this.date = date;
        this.hrsSlept = hrsSlept;
    }

    public String getDate() {
        return date;
    }

    public String getHrsSlept() {
        return hrsSlept;
    }
}
