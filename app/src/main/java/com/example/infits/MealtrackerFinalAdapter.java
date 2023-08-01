package com.example.infits;

        import static android.content.Context.MODE_PRIVATE;

        import android.content.Context;
        import android.content.SharedPreferences;
        import android.os.Handler;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;

        import org.json.JSONArray;
        import org.json.JSONObject;

        import java.util.ArrayList;

public class MealtrackerFinalAdapter extends RecyclerView.Adapter<MealtrackerFinalAdapter.ViewHolder> {

    Context context;
    ArrayList<Todays_BreakFast_info> todays_breakFast_infos;

    public MealtrackerFinalAdapter(Context context, ArrayList<Todays_BreakFast_info> todays_breakFast_infos){
        this.todays_breakFast_infos=todays_breakFast_infos;
        this.context=context;
    }
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.mealtracker_detail,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.icon.setImageDrawable(todays_breakFast_infos.get(position).icon);
//        holder.icon.setImageBitmap(todays_breakFast_infos.get(position).icon);
        holder.mealName.setText(todays_breakFast_infos.get(position).mealName);
        holder.calorieValue.setText(todays_breakFast_infos.get(position).calorieValue);
        holder.fatvalue.setText(todays_breakFast_infos.get(position).fatvalue + " Fat");
        holder.carbsValue.setText(todays_breakFast_infos.get(position).carbsValue + " Carbs");
        holder.quantityValue.setText(todays_breakFast_infos.get(position).quantityValue);
        holder.sizeValue.setText(todays_breakFast_infos.get(position).sizeValue);
        holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context, "clicked for "+todays_breakFast_infos.get(position).mealName, Toast.LENGTH_SHORT).show();
                todays_breakFast_infos.remove(position);
                notifyItemRemoved(position);
                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                },400);
//                notifyDataSetChanged();
                SharedPreferences sharedPreferences = context.getSharedPreferences("TodaysBreakFast", MODE_PRIVATE);
                String jsonString = sharedPreferences.getString("TodaysBreakFast", "");
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray jsonArray = jsonObject.getJSONArray("TodaysBreakFast");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        if (i == position) {
//                            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                            jsonArray.remove(i);
                            break;
                        }
                    }
                    jsonObject.put("TodaysBreakFast", jsonArray);
                    String modifiedJsonString = jsonObject.toString();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("TodaysBreakFast", modifiedJsonString);
                    editor.apply();
                } catch (Exception e) {
                    Log.d("FinalAdapter", "onClick: "+e.toString());
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return todays_breakFast_infos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView icon,deleteIcon;
        TextView mealName, calorieValue, fatvalue, protinValue, carbsValue,  quantityValue, sizeValue;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            icon=itemView.findViewById(R.id.mealImage);
            deleteIcon = itemView.findViewById(R.id.deleteButton);
            mealName=itemView.findViewById(R.id.mealName);
            calorieValue=itemView.findViewById(R.id.calorieValue);
            fatvalue=itemView.findViewById(R.id.fatValue);
            protinValue=itemView.findViewById(R.id.protinValue);
            carbsValue=itemView.findViewById(R.id.carbsValue);
            quantityValue=itemView.findViewById(R.id.quantityValue);
            sizeValue=itemView.findViewById(R.id.sizeValue);
        }
    }
}
