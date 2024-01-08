package com.example.infits;


import android.graphics.drawable.Drawable;

public class Todays_BreakFast_info {
    Drawable icon;

    String mealName, calorieValue, fatvalue, protinValue, carbsValue,  quantityValue, sizeValue;
    public Todays_BreakFast_info(Drawable icon, String mealName, String calorieValue, String fatvalue, String protinValue, String carbsValue,
                                 String quantityValue, String sizeValue){
        this.icon=icon;
        this.mealName=mealName;
        this.calorieValue=calorieValue;
        this.fatvalue=fatvalue;
        this.protinValue=protinValue;
        this.carbsValue=carbsValue;
        this.quantityValue=quantityValue;
        this.sizeValue=sizeValue;
    }

    public Todays_BreakFast_info(String mealName, String calorieValue, String fatvalue, String protinValue, String carbsValue, String quantityValue, String sizeValue) {
        this.mealName = mealName;
        this.calorieValue = calorieValue;
        this.fatvalue = fatvalue;
        this.protinValue = protinValue;
        this.carbsValue = carbsValue;
        this.quantityValue = quantityValue;
        this.sizeValue = sizeValue;

    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public String getCalorieValue() {
        return calorieValue;
    }

    public void setCalorieValue(String calorieValue) {
        this.calorieValue = calorieValue;
    }

    public String getFatValue() {
        return fatvalue;
    }

    public void setFatvalue(String fatvalue) {
        this.fatvalue = fatvalue;
    }

    public String getProteinValue() {
        return protinValue;
    }

    public void setProtinValue(String protinValue) {
        this.protinValue = protinValue;
    }

    public String getCarbsValue() {
        return carbsValue;
    }

    public void setCarbsValue(String carbsValue) {
        this.carbsValue = carbsValue;
    }

    public String getQuantityValue() {
        return quantityValue;
    }

    public void setQuantityValue(String quantityValue) {
        this.quantityValue = quantityValue;
    }

    public String getSizeValue() {
        return sizeValue;
    }

    public void setSizeValue(String sizeValue) {
        this.sizeValue = sizeValue;
    }
}
