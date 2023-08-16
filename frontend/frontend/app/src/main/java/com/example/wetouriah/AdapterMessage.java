package com.example.wetouriah;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterMessage extends RecyclerView.Adapter<MessageViewHolder> {


    Context context;
    List<MessageItem> messageItems;

    public AdapterMessage(Context context, List<MessageItem> messageItems) {
        this.context = context;
        this.messageItems = messageItems;

    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MessageViewHolder(LayoutInflater.from(context).inflate(R.layout.item_message_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
//        holder.id.setText(messageItems.get(position).getId());
        holder.message.setText(messageItems.get(position).getMessage());
        holder.timestamp.setText(messageItems.get(position).getTimestamp());
//        holder.pickup.setText(messageItems.get(position).getPickup());
        holder.sender.setText(messageItems.get(position).getSender());
    }
    @Override
    public int getItemCount() {
        return messageItems.size();
    }






}

