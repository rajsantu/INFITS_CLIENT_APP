package com.example.infits;

public class calorieconsumedInfo {
    String mealName,mealcalorieValue,mealQuantity,mealSize,mealTime,mealType;

    int mealIcon;
    public calorieconsumedInfo(int mealIcon,String mealType,String mealName,String mealcalorieValue,String mealQuantity,String mealSize,
                               String mealTime)
    {
        this.mealType=mealType;
        this.mealIcon=mealIcon;
        this.mealName=mealName;
        this.mealcalorieValue=mealcalorieValue;
        this.mealQuantity=mealQuantity;
        this.mealSize=mealSize;
        this.mealTime=mealTime;
    }
}
