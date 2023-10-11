package com.example.infits;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class Todays_BreakFast_info {
    Bitmap icon;
    Bitmap image;
    String mealName, calorieValue, fatvalue, protinValue, carbsValue,  quantityValue, sizeValue;
    public Todays_BreakFast_info(Bitmap icon, String mealName, String calorieValue, String fatvalue, String protinValue, String carbsValue,
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

    public Todays_BreakFast_info(Bitmap image) {
        this.image = image;
    }
}
