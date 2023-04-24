package com.example.infits;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.reactivex.rxjava3.annotations.NonNull;

public class CalorieInfoAdapter extends RecyclerView.Adapter<CalorieInfoAdapter.ViewHolder> {
    ArrayList<calorieInfo> calorieInfos;
    Context context;
    RecyclerView.RecycledViewPool recycledViewPool=new RecyclerView.RecycledViewPool();
    public CalorieInfoAdapter(Context context,ArrayList<calorieInfo> calorieInfos){
        this.calorieInfos=calorieInfos;
        this.context=context;
    }

    @androidx.annotation.NonNull
    @Override
    public ViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.pastacivitycalorie,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull ViewHolder holder, int position) {
//        Object type=calorieInfos.get(position).ca/
        String color="";
        if(calorieInfos.get(position).Type=="String") {

            String text = context.getString(calorieInfos.get(position).calorieIcon); // replace with your text
            switch (text){
                case "B":
                    color="#FF6262";
                    break;
                case "S":
                    color="#ACAFFD";
                    break;
                case "L":
                    color="#FFAB6E";
                    break;
                case "D":
                    color="#F9FC88";
                    break;

            }
            Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888); // create a bitmap
            Canvas canvas = new Canvas(bitmap); // create a canvas to draw on

            Paint paint = new Paint(); // create a paint object to set text properties
            paint.setColor(Color.parseColor(color)); // set text color
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize(84); // set text size
            paint.setTypeface(Typeface.DEFAULT_BOLD); // set text style
            canvas.drawText(text, 50, 80, paint); // draw the text onto the canvas

            holder.calorieIcon.setImageBitmap(bitmap); // set the bitmap as the image source for the ImageView
            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(holder.calorienestedRecycleview.getContext(),
                    LinearLayoutManager.VERTICAL,false);
            linearLayoutManager.setInitialPrefetchItemCount(calorieInfos.get(position).calorieconsumedInfoList.size());
            MealInfoAdapter mealInfoAdapter=new MealInfoAdapter(context,calorieInfos.get(position).calorieconsumedInfoList);
            holder.calorienestedRecycleview.setLayoutManager(linearLayoutManager);
            holder.calorienestedRecycleview.setAdapter(mealInfoAdapter);
            holder.calorienestedRecycleview.setRecycledViewPool(recycledViewPool);
        }
        else {
            holder.calorieIcon.setImageDrawable(context.getDrawable(calorieInfos.get(position).calorieIcon));
        }
        holder.calorieActivity.setText(calorieInfos.get(position).calorieActivity);
        holder.calorieValue.setText(calorieInfos.get(position).calorieValue);
        holder.calorieActivityTime.setText(calorieInfos.get(position).calorieActivityTime);
        holder.calorieTime.setText(calorieInfos.get(position).calorieTime);
        holder.mainCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });



    }

    @Override
    public int getItemCount() {
        return calorieInfos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView calorieActivity,calorieValue,calorieActivityTime,calorieTime;
        ImageView calorieIcon;
        RecyclerView calorienestedRecycleview;
        CardView mainCardView;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            calorieIcon=itemView.findViewById(R.id.calorieIcon);
            calorieActivity=itemView.findViewById(R.id.calorieActivity);
            calorieValue=itemView.findViewById(R.id.calorieValue);
            calorieActivityTime=itemView.findViewById(R.id.calorieActivityTime);
            calorieTime=itemView.findViewById(R.id.calorieTime);
            calorienestedRecycleview=itemView.findViewById(R.id.calorienestedRecycleview);
            mainCardView=itemView.findViewById(R.id.mainCardView);

        }
    }
}
