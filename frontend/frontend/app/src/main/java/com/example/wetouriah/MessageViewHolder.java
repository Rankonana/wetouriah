package com.example.wetouriah;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MessageViewHolder extends RecyclerView.ViewHolder  {

    TextView message,timestamp,sender;



    public MessageViewHolder(@NonNull View itemView) {
        super(itemView);
        message  = itemView.findViewById(R.id.message);
        timestamp = itemView.findViewById(R.id.timestamp);
        sender = itemView.findViewById(R.id.sender);

    }





}