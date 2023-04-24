package com.example.infits;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private List<Message> messages;

    public MessageAdapter(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.live_screen_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.sender.setText(message.getSender());
        holder.text.setText(message.getText());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView sender;
        private TextView text;

        public ViewHolder(View view) {
            super(view);
            sender = view.findViewById(R.id.userName);
            text = view.findViewById(R.id.userMessage);
        }
    }
}
