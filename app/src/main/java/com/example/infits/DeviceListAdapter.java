package com.example.infits;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.DeviceListViewHolder> {

    private ArrayList<String> deviceNameList;
    private ArrayList<String> deviceAddressList;
    private Context context;
    private int selectedIndex = -1;
    private GetMacInterface getMacInterface;

    DeviceListAdapter(Context context, ArrayList<String> deviceNameList,
                      ArrayList<String> deviceAddressList, GetMacInterface getMacInterface) {
        this.context = context;
        this.deviceNameList = deviceNameList;
        this.deviceAddressList = deviceAddressList;
        this.getMacInterface = getMacInterface;
    }

    @NonNull
    @Override
    public DeviceListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bluetooth_device, parent, false);
        return new DeviceListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceListViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String deviceName = deviceNameList.get(position);
        String deviceAddress = deviceAddressList.get(position);

        holder.name.setText(deviceName);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedIndex = position;
                notifyDataSetChanged();
                getMacInterface.getMac(deviceAddress);
            }
        });

        if (selectedIndex == position) {
            holder.selected.setVisibility(View.VISIBLE);
            holder.linearLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.outline_black));
        } else {
            holder.selected.setVisibility(View.GONE);
            holder.linearLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.outline));
        }
    }


    @Override
    public int getItemCount() {
        return deviceNameList.size();
    }

    public class DeviceListViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView selected;
        LinearLayout linearLayout;

        public DeviceListViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.deviceName);
            selected = itemView.findViewById(R.id.selected);
            linearLayout = itemView.findViewById(R.id.device_pan);
        }
    }
}
