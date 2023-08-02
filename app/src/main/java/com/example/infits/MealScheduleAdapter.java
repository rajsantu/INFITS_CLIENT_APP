package com.example.infits;

import static com.example.infits.MealScheduleItemClass.LayoutOne;
import static com.example.infits.MealScheduleItemClass.LayoutThree;
import static com.example.infits.MealScheduleItemClass.LayoutTwo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MealScheduleAdapter extends RecyclerView.Adapter {
    private List<MealScheduleItemClass> itemClassList;
    public MealScheduleAdapter(List<MealScheduleItemClass> itemClassList){
        this.itemClassList = itemClassList;
    }

    @Override
    public int getItemViewType(int position) {

        switch (itemClassList.get(position).getViewType()) {
            case 0:
                return LayoutOne;
            case 1:
                return LayoutTwo;
            case 2:
                return LayoutThree;
            default:
                return -1;
        }
    }

    //for layout one class
    static class LayoutOneViewHolder extends RecyclerView.ViewHolder{
        private AppCompatImageView appCompatImageView_one;
        private TextView meal_name_one,meal_time_one;

        public LayoutOneViewHolder(@NonNull View itemView)
        {
            super(itemView);
            appCompatImageView_one = itemView.findViewById(R.id.imageMealSchedule);
            meal_name_one = itemView.findViewById(R.id.txtMealName);
            meal_time_one = itemView.findViewById(R.id.txtMealTime);
        }
    }
    static class LayoutTwoViewHolder extends RecyclerView.ViewHolder{
        private TextView meal_name_two,meal_count_two;

        public LayoutTwoViewHolder(@NonNull View itemView)
        {
            super(itemView);
            meal_name_two = itemView.findViewById(R.id.txtBreakfastMeal);
            meal_count_two = itemView.findViewById(R.id.txtMealCount);
        }
    }
    static class LayoutThreeViewHolder extends RecyclerView.ViewHolder{
        private TextView calories_three,caloriesValue_three;
        public LayoutThreeViewHolder(@NonNull View itemView) {
            super(itemView);
            calories_three = itemView.findViewById(R.id.txtThreeCalories);
            caloriesValue_three = itemView.findViewById(R.id.txtThreeCaloriesTotal);
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case LayoutOne:
                View layoutOne
                        = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.meal_schedule_meal_type_layout_one, parent,
                                false);
                return new LayoutOneViewHolder(layoutOne);

            case LayoutTwo:
                View layoutTwo
                        = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.meal_schedule_meal_type_layout_two, parent,
                                false);
                return new LayoutTwoViewHolder(layoutTwo);

            case LayoutThree:
                View layoutThree
                        = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.meal_schedule_meal_type_layout_three, parent,
                                false);
                return new LayoutThreeViewHolder(layoutThree);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (itemClassList.get(position).getViewType()) {
            case LayoutOne:
                ((LayoutOneViewHolder)holder).meal_name_one.setText(itemClassList.get(position).getText_one_layout_one());
                ((LayoutOneViewHolder)holder).meal_time_one.setText(itemClassList.get(position).getText_two_layout_one());
                ((LayoutOneViewHolder)holder).appCompatImageView_one.setOnClickListener(v-> Navigation.findNavController(v).navigate(R.id.action_diet_fourth2_to_fragment_diet_third_scrn));
                break;
            case LayoutTwo:
                ((LayoutTwoViewHolder)holder).meal_name_two.setText(itemClassList.get(position).getText_one_layout_two());
                ((LayoutTwoViewHolder)holder).meal_count_two.setText(itemClassList.get(position).getText_two_layout_two());
                break;
            case LayoutThree:
                ((LayoutThreeViewHolder)holder).calories_three.setText(itemClassList.get(position).getText_one_layout_three());
                ((LayoutThreeViewHolder)holder).caloriesValue_three.setText(itemClassList.get(position).getText_two_layout_three());
                break;

            default:
                return;
        }
    }

    @Override
    public int getItemCount() {
        return itemClassList.size();
    }
}
