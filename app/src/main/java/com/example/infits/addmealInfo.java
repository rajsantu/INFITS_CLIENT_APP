package com.example.infits;

public class addmealInfo {

    int mealIocn,quantity;
    String mealType,mealname,mealcalorie,carb,protein,fat,fiber;

    public addmealInfo(int mealIocn,String mealType,String mealname,String mealcalorie,String carb,String protein,String fat){
        this.mealType=mealType;
        this.mealIocn=mealIocn;
        this.mealname=mealname;
        this.mealcalorie=mealcalorie;
        this.fat=fat;
        this.protein=protein;
        this.carb=carb;
    }

}
