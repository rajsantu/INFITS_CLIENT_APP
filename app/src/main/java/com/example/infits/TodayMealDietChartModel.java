package com.example.infits;



public class TodayMealDietChartModel {
    private final String dietChartMealName,day,time;

    public TodayMealDietChartModel( String dietChartMealName, String day, String time) {
        this.dietChartMealName = dietChartMealName;
        this.day = day;
        this.time = time;
    }
    public String getDietChartMealName() {
        return dietChartMealName;
    }

    public String getDay() {
        return day;
    }

    public String getTime() {
        return time;
    }
}
