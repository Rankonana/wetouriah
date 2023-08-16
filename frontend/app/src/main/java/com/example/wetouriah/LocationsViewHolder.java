package com.example.wetouriah;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LocationsViewHolder extends RecyclerView.ViewHolder  {

    TextView location, timestamp,status;
    public LocationsViewHolder(@NonNull View itemView) {
        super(itemView);


        status = itemView.findViewById(R.id.status);
        location = itemView.findViewById(R.id.location);
        timestamp = itemView.findViewById(R.id.timestamp);

    }

}