package com.example.infits;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CustomeAdapterSpinner extends ArrayAdapter<CustomItem> {
    public CustomeAdapterSpinner(@NonNull Context context, ArrayList<CustomItem> customList) {
        super(context, 0, customList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_spinner_layout, parent, false);
        }

        CustomItem item = getItem(position);
        TextView spinnerTV = convertView.findViewById(R.id.tvSpinnerLayout);
        if(item != null){
            spinnerTV.setText(item.getSpinnerItem());
        }

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_dropdown_layout, parent, false);
        }

        CustomItem item = getItem(position);
        TextView dropDownTV = convertView.findViewById(R.id.tvDropDownLayout);
        if(item != null){
            dropDownTV.setText(item.getSpinnerItem());
        }

        return convertView;
    }
}
