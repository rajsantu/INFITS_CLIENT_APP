package com.example.infits;

import android.graphics.drawable.Drawable;

import java.util.List;

public class calorieInfo {

    String calorieActivity,calorieValue,calorieActivityTime,calorieTime,Type;

    int calorieIcon;
    List<calorieconsumedInfo> calorieconsumedInfoList;
//    String calorieIcon;

    public calorieInfo(int calorieIcon,String Type,String calorieActivity,String calorieValue,
                       String calorieActivityTime,String calorieTime){
        this.calorieIcon=calorieIcon;
        this.Type=Type;
        this.calorieActivity=calorieActivity;
        this.calorieValue=calorieValue;
        this.calorieActivityTime=calorieActivityTime;
        this.calorieTime=calorieTime;
    }
    public calorieInfo(int calorieIcon,String Type,String calorieActivity,String calorieValue,String calorieActivityTime,
                       String calorieTime,List<calorieconsumedInfo> calorieconsumedInfoList){
        this.calorieIcon=calorieIcon;
        this.Type=Type;
        this.calorieActivity=calorieActivity;
        this.calorieValue=calorieValue;
        this.calorieActivityTime=calorieActivityTime;
        this.calorieTime=calorieTime;
        this.calorieconsumedInfoList=calorieconsumedInfoList;
    }

}
