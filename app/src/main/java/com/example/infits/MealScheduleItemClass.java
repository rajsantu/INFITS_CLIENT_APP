package com.example.infits;

public class MealScheduleItemClass {
    public static final int LayoutOne = 0;
    public static final int LayoutTwo = 1;
    public static final int LayoutThree = 2;

    private final int viewType;
    private int icon_layout_one;
    private int icon_layout_three;
    private String text_one_layout_one, text_two_layout_one;
    private String text_one_layout_two, text_two_layout_two;
    private String text_one_layout_three, text_two_layout_three,text_three_layout_three;

    // public constructor for the first layout
    public MealScheduleItemClass(int viewType, int icon,String text1,String text2) {
        this.viewType = viewType;
        this.icon_layout_one = icon;
        this.text_one_layout_one = text1;
        this.text_two_layout_one = text2;
    }

    //public constructor for the second layout
    public MealScheduleItemClass(int viewType,String text_one_layout_two, String text_two_layout_two) {
        this.viewType = viewType;
        this.text_one_layout_two = text_one_layout_two;
        this.text_two_layout_two = text_two_layout_two;
    }

    //public constructor for the third layout
    public MealScheduleItemClass(int viewType, int icon_layout_three, String text_one_layout_three, String text_two_layout_three, String text_three_layout_three) {
        this.viewType = viewType;
        this.icon_layout_three = icon_layout_three;
        this.text_one_layout_three = text_one_layout_three;
        this.text_two_layout_three = text_two_layout_three;
        this.text_three_layout_three = text_three_layout_three;
    }

    public int getIcon_layout_three() {
        return icon_layout_three;
    }

    public String getText_one_layout_three() {
        return text_one_layout_three;
    }

    public String getText_two_layout_three() {
        return text_two_layout_three;
    }

    public String getText_three_layout_three() {
        return text_three_layout_three;
    }



    public int getViewType() {
        return viewType;
    }

    public int getIcon_layout_one() {
        return icon_layout_one;
    }

    public String getText_one_layout_one() {
        return text_one_layout_one;
    }

    public String getText_two_layout_one() {
        return text_two_layout_one;
    }

    public String getText_one_layout_two() {
        return text_one_layout_two;
    }

    public String getText_two_layout_two() {
        return text_two_layout_two;
    }
}
