package com.example.infits;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReferralActiveUsersAdapter extends RecyclerView.Adapter<ReferralActiveUsersAdapter.ViewHolder> {

    String[] activeUsers;
    ArrayList<String> profiles;
    Context context;

    public ReferralActiveUsersAdapter(String[] activeUsers, ArrayList<String> profiles, Context context) {
        this.activeUsers = activeUsers;
        this.profiles = profiles;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.referral_active_users_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name = activeUsers[position];
        Bitmap profile = getBitmap(profiles.get(position));

        holder.name.setText(name);
        holder.profile.setImageBitmap(profile);
    }

    @Override
    public int getItemCount() {
        return activeUsers.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profile;
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.profile);
            name = itemView.findViewById(R.id.name);
        }
    }

    Bitmap getBitmap(String encodedImage) {
        byte[] imageBytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }
}
