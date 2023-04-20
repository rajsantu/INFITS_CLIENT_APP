package com.example.infits;

        import android.content.Context;
        import android.content.Intent;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageButton;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import androidx.annotation.NonNull;
        import androidx.cardview.widget.CardView;
        import androidx.navigation.Navigation;
        import androidx.recyclerview.widget.RecyclerView;

        import java.util.ArrayList;

public class MealtrackerAddMealAdapter extends RecyclerView.Adapter<MealtrackerAddMealAdapter.ViewHolder> {

    Context context;
    ArrayList<addmealInfo> filterItems;
    ArrayList<addmealInfo> addmealInfos;
    ArrayList<String> mealInfotransfer;

    public MealtrackerAddMealAdapter(Context context, ArrayList<addmealInfo> addmealInfos){
        this.context=context;
        this.addmealInfos=addmealInfos;
        this.filterItems=addmealInfos;
        mealInfotransfer=new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.mealtracker_add_meal,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        holder.addmealIcon.setImageDrawable(context.getDrawable(addmealInfos.get(position).mealIocn));
        holder.addMealName.setText(addmealInfos.get(position).mealname);
//        holder.addMealQuantity.setText(addmealInfos.get(position).mealQuantity);
//        holder.addMealCalorie.setText(addmealInfos.get(position).mealcalorie);
//        holder.addMealWeight.setText(addmealInfos.get(position).mealWeight);
        mealInfotransfer.clear();
        holder.addMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                int icon=addmealInfos.get(position).mealIocn;
                String Meal_Name=addmealInfos.get(position).mealname;
                String Meal_type=addmealInfos.get(position).mealType;
                String calorie=addmealInfos.get(position).mealcalorie;
                String carbs=addmealInfos.get(position).carb;
                String protin=addmealInfos.get(position).protein;
                String fat=addmealInfos.get(position).fat;
                mealInfotransfer.add(Meal_Name);
                mealInfotransfer.add(Meal_type);
                mealInfotransfer.add(calorie);
                mealInfotransfer.add(carbs);
                mealInfotransfer.add(protin);
                mealInfotransfer.add(fat);
                mealInfotransfer.add(String.valueOf(icon));
                Log.d("mealInfotransfer",mealInfotransfer.toString());

                bundle.putStringArrayList("mealInfotransfer",mealInfotransfer);
                Intent intent = new Intent(context, Activity_Todays_Breakfast.class);
                intent.putExtra("bundle",bundle);
                intent.putExtra("fragment", "mealinfowithphoto");
                context.startActivity(intent);
//                Toast.makeText(context, "Activity", Toast.LENGTH_SHORT).show();


//                if (addmealInfos.get(position).mealType == "BreakFast") {
//                    Navigation.findNavController(v).navigate(R.id.action_calorieAddBreakfastFragment_to_mealInfoWithPhoto, bundle);
//
//                }
//                if (addmealInfos.get(position).mealType == "Lunch") {
//                    Navigation.findNavController(v).navigate(R.id.action_calorieAddLunchFragment_to_mealInfoWithPhoto, bundle);
//                }
//                if (addmealInfos.get(position).mealType == "Dinner") {
//                    Navigation.findNavController(v).navigate(R.id.action_calorieAddDinnerFragment_to_mealInfoWithPhoto, bundle);
//                }
//                if (addmealInfos.get(position).mealType == "Snacks") {
//                    Navigation.findNavController(v).navigate(R.id.action_calorieAddSnacksFragment_to_mealInfoWithPhoto, bundle);
//                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return addmealInfos.size();
    }
    public void filterList(ArrayList<addmealInfo> filterlist) {
        // below line is to add our filtered
        // list in our course array list.
        addmealInfos = filterlist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        CardView addMealButton;
        ImageView addmealIcon;
        TextView addMealName;
        public ViewHolder(View itemView){
            super(itemView);
            addMealButton=itemView.findViewById(R.id.addMealButton);
            addmealIcon=itemView.findViewById(R.id.addmealIcon);
            addMealName=itemView.findViewById(R.id.addMealName);
//            addMealCalorie=itemView.findViewById(R.id.addMealCalorie);
//            addMealQuantity= itemView.findViewById(R.id.addMealQuantity);
//            addMealWeight=itemView.findViewById(R.id.addMealWeight);
        }
    }
}
